package com.example.booking.web;

import java.security.Principal;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.booking.model.AppUser;
import com.example.booking.repository.UserRepository;

/** Exposes the logged-in account to every template as {@code currentUser} (null for guests). */
@ControllerAdvice
public class CurrentUserAdvice {

	private final UserRepository users;

	public CurrentUserAdvice(UserRepository users) {
		this.users = users;
	}

	@ModelAttribute("currentUser")
	public AppUser currentUser(Principal principal) {
		return principal == null ? null : users.findByEmailIgnoreCase(principal.getName()).orElse(null);
	}

}
