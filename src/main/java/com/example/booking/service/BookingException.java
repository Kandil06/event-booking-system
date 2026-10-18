package com.example.booking.service;

/** A booking or event change was refused; the message is safe to show to the user. */
public class BookingException extends RuntimeException {

	public BookingException(String message) {
		super(message);
	}

}
