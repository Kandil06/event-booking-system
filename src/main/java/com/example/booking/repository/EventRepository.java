package com.example.booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.booking.model.Event;

import jakarta.persistence.LockModeType;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findAllByOrderByStartsAtAsc();

	/** Locks the event row so concurrent bookings cannot oversell it. */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select e from Event e where e.id = :id")
	Optional<Event> findByIdForUpdate(@Param("id") Long id);

}
