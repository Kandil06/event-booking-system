package com.example.booking.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.booking.model.Ticket;
import com.example.booking.service.BookingException;
import com.example.booking.service.BookingService;
import com.example.booking.service.EventService;

import jakarta.validation.Valid;

/** Admin-only pages; access is restricted to ROLE_ADMIN in SecurityConfig. */
@Controller
@RequestMapping("/admin")
public class AdminController {

	private final EventService events;
	private final BookingService bookings;

	public AdminController(EventService events, BookingService bookings) {
		this.events = events;
		this.bookings = bookings;
	}

	@GetMapping({ "", "/events" })
	public String list(Model model) {
		model.addAttribute("events", events.list());
		return "admin/events";
	}

	@GetMapping("/events/new")
	public String createForm(Model model) {
		model.addAttribute("eventForm", new EventForm());
		return "admin/event-form";
	}

	@PostMapping("/events")
	public String create(@Valid @ModelAttribute("eventForm") EventForm form, BindingResult result,
			RedirectAttributes redirect) {
		return save(null, form, result, redirect);
	}

	@GetMapping("/events/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("eventForm", EventForm.of(events.get(id)));
		model.addAttribute("eventId", id);
		return "admin/event-form";
	}

	@PostMapping("/events/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("eventForm") EventForm form,
			BindingResult result, Model model, RedirectAttributes redirect) {
		model.addAttribute("eventId", id);
		return save(id, form, result, redirect);
	}

	@PostMapping("/events/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirect) {
		try {
			events.delete(id);
			redirect.addFlashAttribute("success", "Event deleted.");
		} catch (BookingException e) {
			redirect.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/admin/events";
	}

	@GetMapping("/events/{id}/bookings")
	public String eventBookings(@PathVariable Long id, Model model) {
		model.addAttribute("view", events.view(id));
		model.addAttribute("bookings", bookings.forEvent(id));
		return "admin/event-bookings";
	}

	@PostMapping("/tickets/{id}/toggle-used")
	public String toggleUsed(@PathVariable Long id) {
		Ticket ticket = bookings.toggleUsed(id);
		return "redirect:/admin/events/" + ticket.getEvent().getId() + "/bookings";
	}

	private String save(Long id, EventForm form, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "admin/event-form";
		}
		try {
			events.save(id, form.getTitle().trim(), form.getDescription(), form.getLocation().trim(),
					form.getStartsAt(), form.getCapacity());
		} catch (BookingException e) {
			result.rejectValue("capacity", "tooLow", e.getMessage());
			return "admin/event-form";
		}
		redirect.addFlashAttribute("success", id == null ? "Event created." : "Event updated.");
		return "redirect:/admin/events";
	}

}
