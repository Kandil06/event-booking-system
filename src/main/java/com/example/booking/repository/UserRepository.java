package com.example.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking.model.AppUser;
import com.example.booking.model.Role;

public interface UserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByRole(Role role);

}
