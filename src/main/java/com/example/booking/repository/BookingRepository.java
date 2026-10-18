package com.example.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

	List<Booking> findByEventIdOrderByCreatedAtDesc(Long eventId);

}
