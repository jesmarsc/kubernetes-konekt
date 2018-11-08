package com.kubernetes.konekt.controller;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubernetes.konekt.entity.UserAccount;

@Controller
public class LoginController {
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@RequestMapping(value = "user/Kevin")
	public String showUserDashboard(Model model) {
		return "user-dashboard";
	}
	
	@RequestMapping(value = "provider/Kevin")
	public String showProviderDashboard(Model model) {
		return "provider-dashboard";
	}
	
	/*
	@RequestMapping(value = "/login")
	public String showLogin(Model model) {
		return "login-form";
	}
	
	@RequestMapping(value = "/login-confirmation")
	public String processLogin(@Valid @ModelAttribute("user") UserAccount user,
			BindingResult validation, Model model) {
		if(validation.hasErrors()) {
			return "login-form";
		}
		else {
			return "login-confirmation";
		}
	}
	*/

}
