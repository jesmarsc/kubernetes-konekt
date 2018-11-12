package com.kubernetes.konekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@RequestMapping(value = "/user")
	public String showUserDashboard(Model model) {
		return "user-dashboard";
	}
	
	@RequestMapping(value = "/provider")
	public String showProviderDashboard(Model model) {
		return "provider-dashboard";
	}
	
	@RequestMapping(value = "/login")
	public String showLogin() {
		return "login-form";
	}
	
	@RequestMapping(value = "/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
}
