package com.example.booking.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.booking.model.Event;
import com.example.booking.repository.EventRepository;
import com.example.booking.repository.TicketRepository;

@Service
public class EventService {

	private final EventRepository events;
	private final TicketRepository tickets;

	public EventService(EventRepository events, TicketRepository tickets) {
		this.events = events;
		this.tickets = tickets;
	}

	public List<EventView> list() {
		Map<Long, Long> booked = new HashMap<>();
		for (Object[] row : tickets.countTicketsPerEvent()) {
			booked.put((Long) row[0], (Long) row[1]);
		}
		return events.findAllByOrderByStartsAtAsc().stream()
				.map(e -> new EventView(e, booked.getOrDefault(e.getId(), 0L)))
				.toList();
	}

	public Event get(Long id) {
		return events.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public EventView view(Long id) {
		return new EventView(get(id), tickets.countByEventId(id));
	}

	/** Creates the event when {@code id} is null, otherwise updates it. */
	@Transactional
	public Event save(Long id, String title, String description, String location, LocalDateTime startsAt,
			int capacity) {
		Event event;
		if (id == null) {
			event = new Event();
		} else {
			event = events.findByIdForUpdate(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			long booked = tickets.countByEventId(id);
			if (capacity < booked) {
				throw new BookingException(
						booked + " tickets are already booked, so the limit cannot be lower than that.");
			}
		}
		event.setTitle(title);
		event.setDescription(description);
		event.setLocation(location);
		event.setStartsAt(startsAt);
		event.setCapacity(capacity);
		return events.save(event);
	}

	@Transactional
	public void delete(Long id) {
		Event event = events.findByIdForUpdate(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (tickets.countByEventId(id) > 0) {
			throw new BookingException("\"" + event.getTitle() + "\" has bookings and cannot be deleted.");
		}
		events.delete(event);
	}

}
