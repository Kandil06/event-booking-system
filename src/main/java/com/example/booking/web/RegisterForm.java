package com.example.booking.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {

	@NotBlank(message = "Please enter your name")
	@Size(max = 100)
	private String fullName;

	@NotBlank(message = "Please enter your email")
	@Email(message = "Please enter a valid email address")
	private String email;

	@NotBlank(message = "Please choose a password")
	@Size(min = 6, max = 72, message = "Password must be 6 to 72 characters")
	private String password;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
