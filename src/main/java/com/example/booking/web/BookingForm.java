package com.example.booking.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookingForm {

	@NotBlank(message = "Please enter the email to send your tickets to")
	@Email(message = "Please enter a valid email address")
	private String email;

	@NotNull(message = "Please choose how many tickets you want")
	@Min(value = 1, message = "Book at least 1 ticket")
	private Integer quantity = 1;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
