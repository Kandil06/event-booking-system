package com.example.booking.service;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.booking.model.Booking;
import com.example.booking.model.Event;
import com.example.booking.model.Ticket;

@Service
public class TicketMailService {

	private static final Logger log = LoggerFactory.getLogger(TicketMailService.class);
	private static final DateTimeFormatter WHEN = DateTimeFormatter.ofPattern("EEE d MMM yyyy 'at' HH:mm");

	// Absent until spring.mail.host is configured
	private final ObjectProvider<JavaMailSender> mailSender;
	private final String from;

	public TicketMailService(ObjectProvider<JavaMailSender> mailSender,
			@Value("${app.mail.from:no-reply@booking.local}") String from) {
		this.mailSender = mailSender;
		this.from = from;
	}

	/**
	 * Emails the booking's ticket codes to the address given at booking time.
	 *
	 * @return true if the email was handed to the mail server
	 */
	public boolean sendTickets(Booking booking) {
		Event event = booking.getEvent();
		StringBuilder body = new StringBuilder();
		body.append("Hello ").append(booking.getUser().getFullName()).append(",\n\n");
		body.append("Your booking is confirmed.\n\n");
		body.append("Event:    ").append(event.getTitle()).append('\n');
		body.append("When:     ").append(event.getStartsAt().format(WHEN)).append('\n');
		body.append("Where:    ").append(event.getLocation()).append("\n\n");
		body.append("Your ticket code(s):\n");
		for (Ticket ticket : booking.getTickets()) {
			body.append("  ").append(ticket.getCode()).append('\n');
		}
		body.append("\nShow a code at the entrance. Each code admits one person.\n");

		JavaMailSender sender = mailSender.getIfAvailable();
		if (sender == null) {
			log.info("Mail is not configured (spring.mail.host); ticket email for {} not sent:\n{}",
					booking.getEmail(), body);
			return false;
		}
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(booking.getEmail());
			message.setSubject("Your tickets for " + event.getTitle());
			message.setText(body.toString());
			sender.send(message);
			return true;
		} catch (Exception e) {
			log.error("Could not send ticket email for booking {} to {}", booking.getId(), booking.getEmail(), e);
			return false;
		}
	}

}
