package com.example.booking.web;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.booking.model.Event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventForm {

	@NotBlank(message = "Please enter a title")
	@Size(max = 255)
	private String title;

	@Size(max = 2000, message = "Description can be at most 2000 characters")
	private String description;

	@NotBlank(message = "Please enter a location")
	@Size(max = 255)
	private String location;

	// Matches the value format of <input type="datetime-local">
	@NotNull(message = "Please choose a date and time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startsAt;

	@NotNull(message = "Please enter how many tickets are allowed")
	@Min(value = 1, message = "At least 1 ticket must be allowed")
	private Integer capacity;

	public static EventForm of(Event event) {
		EventForm form = new EventForm();
		form.title = event.getTitle();
		form.description = event.getDescription();
		form.location = event.getLocation();
		form.startsAt = event.getStartsAt();
		form.capacity = event.getCapacity();
		return form;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(LocalDateTime startsAt) {
		this.startsAt = startsAt;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

}
