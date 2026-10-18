package com.example.booking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.booking.model.Role;
import com.example.booking.service.UserService;

/** Creates the first admin account from app.admin.* when no admin exists yet. */
@Component
public class AdminSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

	private final UserService users;
	private final String email;
	private final String password;

	public AdminSeeder(UserService users, @Value("${app.admin.email}") String email,
			@Value("${app.admin.password}") String password) {
		this.users = users;
		this.email = email;
		this.password = password;
	}

	@Override
	public void run(String... args) {
		if (users.adminExists() || users.emailTaken(email)) {
			return;
		}
		users.register("Administrator", email, password, Role.ADMIN);
		log.info("Created admin account {}", email);
	}

}
