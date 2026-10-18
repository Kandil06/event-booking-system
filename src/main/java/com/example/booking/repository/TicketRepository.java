package com.example.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.booking.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	long countByEventId(Long eventId);

	boolean existsByCode(String code);

	/** Rows of [eventId, ticketCount] for every event that has tickets. */
	@Query("select t.event.id, count(t) from Ticket t group by t.event.id")
	List<Object[]> countTicketsPerEvent();

}
