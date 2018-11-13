package com.kubernetes.konekt.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.form.UploadClusterForm;
import com.kubernetes.konekt.form.UploadContainerToClusterForm;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;

@Controller
public class UploadController {

	@Autowired
	AccountService accountService;
	@Autowired
	ClusterService clusterSevice;
	
	@RequestMapping(value = "/uploadContainerToClusterConfirmation")
	public String uploadContainerToCluster( @ModelAttribute("uploadForm") UploadContainerToClusterForm uploadForm, Model model) {
		
		model.addAttribute("containerName", uploadForm.getContainerName());
		model.addAttribute("clusterIp", uploadForm.getClusterIp());
		return "user/container-to-cluster-upload-confirmation";
	}
	
	@RequestMapping(value = "/uploadClusterConfirmation")
	public String uploadNewCluster(@Valid @ModelAttribute("newClusterForm") UploadClusterForm uploadClusterForm,BindingResult theBindingResult, Model model) {
		
		if(theBindingResult.hasErrors()) {
			return "provider/provider-dashboard";
		}
		
		if(clusterSevice.doesClusterExist(uploadClusterForm.getClusterIp())){
			System.out.println("\n\n\n\n ip already exist \n\n\n\n");
			String message = "Invalid IP address. IP address is already in the database";
			model.addAttribute("message",message);
			return "redirect:/provider";
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		
		Cluster newCluster = new Cluster(uploadClusterForm.getClusterIp());
		System.out.println("\n\n\n\\n\n\n"+"|" + newCluster + "|" + "\n\n\n\n\n");
		currentAccount.addCluster(newCluster);
		
		// For some reason if database is not accessed after cluster is added to 
		// user the cluster is not saved to the user.
		List<Account> accounts = accountService.getAccounts();
		accounts.contains(currentAccount);
		
		model.addAttribute("clusterIp", uploadClusterForm.getClusterIp());
		return "provider/cluster-upload-confirmation";
	}
}
