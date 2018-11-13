package com.kubernetes.konekt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;

@Controller
public class LoginController {
	
	@Autowired
	AccountService accountService;
	@Autowired 
	ClusterService clusterService;
	
	@RequestMapping(value = "/user")
	public String showUserDashboard(Model model) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);
		
		List<Cluster> availableClusters = clusterService.getAllClusters();
		System.out.println("\n\n\n\n" + availableClusters + "\n\n\n\n");
		model.addAttribute("availableClusters", availableClusters);
		
		return "user/user-dashboard";
	}
	
	@RequestMapping(value = "/provider")
	public String showProviderDashboard(Model model) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);
		

		return "provider/provider-dashboard";
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
