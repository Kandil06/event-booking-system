package com.example.booking.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.booking.model.Role;
import com.example.booking.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	private final UserService users;

	public AuthController(UserService users) {
		this.users = users;
	}

	@GetMapping("/")
	public String home() {
		return "redirect:/events";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult result,
			RedirectAttributes redirect) {
		if (!result.hasFieldErrors("email") && users.emailTaken(form.getEmail())) {
			result.rejectValue("email", "taken", "An account with this email already exists");
		}
		if (result.hasErrors()) {
			return "register";
		}
		users.register(form.getFullName(), form.getEmail(), form.getPassword(), Role.USER);
		redirect.addFlashAttribute("success", "Account created. You can log in now.");
		return "redirect:/login";
	}

}
