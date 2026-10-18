package com.example.booking.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.booking.model.AppUser;
import com.example.booking.model.Booking;
import com.example.booking.service.BookingException;
import com.example.booking.service.BookingService;
import com.example.booking.service.EventService;
import com.example.booking.service.EventView;
import com.example.booking.service.TicketMailService;
import com.example.booking.service.UserService;

import jakarta.validation.Valid;

@Controller
public class EventController {

	private final EventService events;
	private final BookingService bookings;
	private final TicketMailService mail;
	private final UserService users;

	public EventController(EventService events, BookingService bookings, TicketMailService mail,
			UserService users) {
		this.events = events;
		this.bookings = bookings;
		this.mail = mail;
		this.users = users;
	}

	@GetMapping("/events")
	public String list(Model model) {
		model.addAttribute("events", events.list());
		return "events";
	}

	@GetMapping("/events/{id}")
	public String detail(@PathVariable Long id, Principal principal, Model model) {
		BookingForm form = new BookingForm();
		if (principal != null) {
			form.setEmail(principal.getName());
		}
		model.addAttribute("bookingForm", form);
		return showDetail(id, model);
	}

	@PostMapping("/events/{id}/book")
	public String book(@PathVariable Long id, @Valid @ModelAttribute("bookingForm") BookingForm form,
			BindingResult result, Principal principal, Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return showDetail(id, model);
		}
		AppUser user = users.require(principal.getName());
		Booking booking;
		try {
			booking = bookings.book(user, id, form.getEmail().trim(), form.getQuantity());
		} catch (BookingException e) {
			model.addAttribute("error", e.getMessage());
			return showDetail(id, model);
		}
		if (mail.sendTickets(booking)) {
			redirect.addFlashAttribute("success", "Booking confirmed. Your ticket codes were sent to "
					+ booking.getEmail() + ".");
		} else {
			redirect.addFlashAttribute("error", "Booking confirmed, but the email to " + booking.getEmail()
					+ " could not be sent. Your ticket codes are shown below.");
		}
		return "redirect:/bookings/" + booking.getId();
	}

	@GetMapping("/bookings")
	public String myBookings(Principal principal, Model model) {
		model.addAttribute("bookings", bookings.forUser(users.require(principal.getName())));
		return "bookings";
	}

	@GetMapping("/bookings/{id}")
	public String booking(@PathVariable Long id, Principal principal, Model model) {
		model.addAttribute("booking", bookings.getFor(id, users.require(principal.getName())));
		return "booking";
	}

	private String showDetail(Long id, Model model) {
		EventView view = events.view(id);
		model.addAttribute("view", view);
		model.addAttribute("maxQuantity", Math.min(bookings.getMaxPerBooking(), view.getRemaining()));
		return "event";
	}

}
