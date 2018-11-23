package com.kubernetes.konekt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.service.AccountService;

@Controller
@RequestMapping("/user")
public class AccountListController {

	@Autowired
	private AccountService accountService;
	
	@RequestMapping("/list")
	public String listUserAccounts(Model theModel) {

		List<Account> accounts = accountService.getAccounts();
		
		theModel.addAttribute("accounts", accounts);
		
		return "list-accounts";
	}
	
}


