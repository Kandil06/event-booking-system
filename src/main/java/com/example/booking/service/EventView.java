package com.example.booking.service;

import com.example.booking.model.Event;

/** An event together with how many of its tickets are already booked. */
public class EventView {

	private final Event event;
	private final long booked;

	public EventView(Event event, long booked) {
		this.event = event;
		this.booked = booked;
	}

	public Event getEvent() {
		return event;
	}

	public long getBooked() {
		return booked;
	}

	public long getRemaining() {
		return Math.max(0, event.getCapacity() - booked);
	}

	public boolean isSoldOut() {
		return getRemaining() == 0;
	}

	public boolean isBookable() {
		return !isSoldOut() && !event.isPast();
	}

	/** Share of capacity booked, 0-100, for the progress bar. */
	public int getPercentBooked() {
		return event.getCapacity() == 0 ? 100 : (int) Math.min(100, booked * 100 / event.getCapacity());
	}

}
