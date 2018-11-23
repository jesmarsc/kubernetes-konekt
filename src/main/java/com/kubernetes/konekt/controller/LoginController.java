package com.kubernetes.konekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@RequestMapping(value = "/login")
	public String showLogin() {
		return "login-form";
	}
	
	@RequestMapping(value = "/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
}
