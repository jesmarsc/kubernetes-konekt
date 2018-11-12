package com.kubernetes.konekt.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.RegistrationForm;
import com.kubernetes.konekt.service.AccountService;


@Controller
public class RegisterController {
	
	@Autowired
	private AccountService accountService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@RequestMapping(value="/register")
	public String ShowForm(Model theModel) {
		RegistrationForm newForm = new RegistrationForm();
		theModel.addAttribute("registrationForm", newForm);
		return "registration-form";
	}


	@PostMapping(value="/accountConfirmation")
	public String details(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
			BindingResult theBindingResult, Model model) {
		
		if(theBindingResult.hasErrors()) {
			return "registration-form";
		}
		
		if (!form.getEmail().equals(form.getConfirmEmail())){
			String message = "Email and Confirm Email do not match";
			model.addAttribute("message", message);
			return "registration-form";
		}
		
		else if(!form.getPassword().equals(form.getConfirmPassword())) {
			String message = "Password and Confirm Password do not match";
			model.addAttribute("message", message);
			return "registration-form";
		}
		
		// check the database if user already exists
        Account existing = accountService.findByUserName(form.getUserName());
        if (existing != null){
        	model.addAttribute("registrationForm", new RegistrationForm());
			model.addAttribute("registrationError", "User name already exists.");
        	return "registration-form";
        }
		
		boolean didAdd = accountService.saveAccount(form);
		
		if(!didAdd) {
			String message = "Account with email provided already exists";
			model.addAttribute("message", message);
			return "registration-form";	
		}
		
		return "registration-confirmation";
	}
}


