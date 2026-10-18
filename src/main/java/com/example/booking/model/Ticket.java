package com.example.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String code;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Booking booking;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Event event;

	/** Set by an admin when the ticket is checked in at the door. */
	@Column(nullable = false)
	private boolean used;

	protected Ticket() {
	}

	Ticket(String code, Booking booking, Event event) {
		this.code = code;
		this.booking = booking;
		this.event = event;
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public Booking getBooking() {
		return booking;
	}

	public Event getEvent() {
		return event;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
