package com.example.booking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/** One order by a user for one event; holds one ticket per seat. */
@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private AppUser user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Event event;

	/** Address the ticket codes are sent to; may differ from the account email. */
	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("id")
	private List<Ticket> tickets = new ArrayList<>();

	protected Booking() {
	}

	public Booking(AppUser user, Event event, String email) {
		this.user = user;
		this.event = event;
		this.email = email;
	}

	public void addTicket(String code) {
		tickets.add(new Ticket(code, this, event));
	}

	public Long getId() {
		return id;
	}

	public AppUser getUser() {
		return user;
	}

	public Event getEvent() {
		return event;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

}
