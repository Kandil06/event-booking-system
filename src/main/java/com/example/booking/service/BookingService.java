package com.example.booking.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.booking.model.AppUser;
import com.example.booking.model.Booking;
import com.example.booking.model.Event;
import com.example.booking.model.Ticket;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.EventRepository;
import com.example.booking.repository.TicketRepository;

@Service
public class BookingService {

	private final EventRepository events;
	private final BookingRepository bookings;
	private final TicketRepository tickets;
	private final TicketCodeGenerator codes;
	private final int maxPerBooking;

	public BookingService(EventRepository events, BookingRepository bookings, TicketRepository tickets,
			TicketCodeGenerator codes, @Value("${app.booking.max-per-booking:10}") int maxPerBooking) {
		this.events = events;
		this.bookings = bookings;
		this.tickets = tickets;
		this.codes = codes;
		this.maxPerBooking = maxPerBooking;
	}

	public int getMaxPerBooking() {
		return maxPerBooking;
	}

	/** Books {@code quantity} tickets, generating one code per ticket. */
	@Transactional
	public Booking book(AppUser user, Long eventId, String email, int quantity) {
		if (quantity < 1 || quantity > maxPerBooking) {
			throw new BookingException("You can book between 1 and " + maxPerBooking + " tickets at a time.");
		}
		Event event = events.findByIdForUpdate(eventId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (event.isPast()) {
			throw new BookingException("This event has already taken place.");
		}
		long remaining = event.getCapacity() - tickets.countByEventId(eventId);
		if (remaining <= 0) {
			throw new BookingException("Sorry, this event is sold out.");
		}
		if (quantity > remaining) {
			throw new BookingException("Only " + remaining + " ticket(s) left for this event.");
		}

		Booking booking = new Booking(user, event, email);
		Set<String> issued = new HashSet<>();
		while (issued.size() < quantity) {
			String code = codes.generate(event);
			if (issued.add(code)) {
				booking.addTicket(code);
			}
		}
		return bookings.save(booking);
	}

	public List<Booking> forUser(AppUser user) {
		return bookings.findByUserIdOrderByCreatedAtDesc(user.getId());
	}

	public List<Booking> forEvent(Long eventId) {
		return bookings.findByEventIdOrderByCreatedAtDesc(eventId);
	}

	/** Returns the booking if it belongs to the user (admins may see any). */
	public Booking getFor(Long bookingId, AppUser user) {
		Booking booking = bookings.findById(bookingId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (!user.isAdmin() && !booking.getUser().getId().equals(user.getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return booking;
	}

	/** Flips a ticket between checked-in and not; returns the ticket. */
	@Transactional
	public Ticket toggleUsed(Long ticketId) {
		Ticket ticket = tickets.findById(ticketId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		ticket.setUsed(!ticket.isUsed());
		return ticket;
	}

}
