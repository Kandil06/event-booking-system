package com.example.booking.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.example.booking.model.Event;
import com.example.booking.repository.TicketRepository;

/**
 * Generates ticket codes tied to their event, e.g. {@code EV0007-K3XW9QTM}:
 * the prefix is the event id, the suffix is random and unique.
 */
@Component
public class TicketCodeGenerator {

	// No 0/O or 1/I, so codes are easy to read out and type
	private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
	private static final int SUFFIX_LENGTH = 8;

	private final SecureRandom random = new SecureRandom();
	private final TicketRepository tickets;

	public TicketCodeGenerator(TicketRepository tickets) {
		this.tickets = tickets;
	}

	public String generate(Event event) {
		String code;
		do {
			StringBuilder sb = new StringBuilder(String.format("EV%04d-", event.getId()));
			for (int i = 0; i < SUFFIX_LENGTH; i++) {
				sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
			}
			code = sb.toString();
		} while (tickets.existsByCode(code));
		return code;
	}

}
