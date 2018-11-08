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

import com.kubernetes.konekt.dao.UserAccountDAO;
import com.kubernetes.konekt.entity.UserAccount;
import com.kubernetes.konekt.entity.UserRegistration;


@Controller
public class RegisterController {
	
	@Autowired
	private UserAccountDAO userDAO;
	
	
	
	
	
	
	
	// add an initbinder to convert trim inputs to strings
	// remove leading and trailing whitespace
	// resolve issue for validation
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@RequestMapping(value="/register")
	public String ShowForm(Model theModel) {
		
		// create new userResgistration object
		UserRegistration newUser = new UserRegistration();
	
		// add userRegistration object to model
		theModel.addAttribute("userRegistration", newUser);

		return "registration-form";
	}


	@PostMapping(value="/accountConfirmation")
	public String details(@Valid @ModelAttribute("userRegistration") UserRegistration newUser, BindingResult theBindingResult, Model model) {
		
		if(theBindingResult.hasErrors()) {
			return "registration-form";
		}
		/* TEST
		 * 
		 * whitespace not accepted
		 * 
		 * fields are left empty
		 * 
		 * emails match but passwords do not
		 *
		 * passwords match but emails do not
		 * 
		 * neither password nor email match 
		 * 
		 * both emails and passwords match 
		 */
		// confirm passwords match 
		// confirm email match 
		
		
		// if email and confirm email or password and confirm password do not match 
		// redirect user back to registration page
		// TODO: add error message to let user know why account was not created
		if (!newUser.getEmail().equals(newUser.getConfirmEmail())){
			String message = "Email and Confirm Email do not match";
			model.addAttribute("message", message);
			return "registration-form";
		}
		
		else if(!newUser.getPassword().equals(newUser.getConfirmPassword())) {
			String message = "Password and Confirm Password do not match";
			model.addAttribute("message", message);
			return "registration-form";
			
		}
		
		// else
		

		// create user object
		UserAccount newAccount = new UserAccount();
		
		//translating registration information into user information
		newAccount.getUserFromRegistration(newUser);
		
		// save data to database
		boolean didAdd = userDAO.saveUser(newAccount);
		
		if(!didAdd) {
			String message = "Account with email provided already exist";
			model.addAttribute("message", message);
			return "registration-form";	
		}
		
		// direct user to confirmation page or welcome/dashboard page
		return "registration-confirmation";
	}
}


