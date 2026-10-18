package com.example.booking.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.booking.model.AppUser;
import com.example.booking.model.Role;
import com.example.booking.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository users;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository users, PasswordEncoder passwordEncoder) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser user = users.findByEmailIgnoreCase(email.trim())
				.orElseThrow(() -> new UsernameNotFoundException(email));
		return User.withUsername(user.getEmail())
				.password(user.getPasswordHash())
				.roles(user.getRole().name())
				.build();
	}

	public boolean emailTaken(String email) {
		return users.existsByEmailIgnoreCase(email.trim());
	}

	public AppUser register(String fullName, String email, String rawPassword, Role role) {
		return users.save(new AppUser(fullName.trim(), email.trim().toLowerCase(),
				passwordEncoder.encode(rawPassword), role));
	}

	public boolean adminExists() {
		return users.existsByRole(Role.ADMIN);
	}

	/** The logged-in user's account, looked up by the login name (email). */
	public AppUser require(String email) {
		return users.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
	}

}
