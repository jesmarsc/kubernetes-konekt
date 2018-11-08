package com.kubernetes.konekt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubernetes.konekt.dao.UserAccountDAO;
import com.kubernetes.konekt.entity.UserAccount;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserAccountDAO userDAO;
	
	@RequestMapping("/list")
	public String listUserAccounts(Model theModel) {
		
		List<UserAccount> theUsers = userDAO.getUserAccounts();
		
		theModel.addAttribute("userAccounts", theUsers);
		
		return "list-users";
	}
	
}


