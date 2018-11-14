package com.kubernetes.konekt.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	ClusterService clusterService;
	
	@RequestMapping(value = "/uploadContainerToClusterConfirmation")
	public String uploadContainerToCluster( @ModelAttribute("uploadForm") UploadContainerToClusterForm uploadForm, Model model) {
		
		model.addAttribute("containerName", uploadForm.getContainerName());
		model.addAttribute("clusterIp", uploadForm.getClusterIp());
		return "user/container-to-cluster-upload-confirmation";
	}
	
	
	@RequestMapping(value = "/deleteConfirmation")
	public String deleteCluster( @RequestParam("clusterIp") String clusterIp,Model model) {
		
		Cluster TBDeletedCluster = clusterService.getCluster(clusterIp);
		clusterService.deleteCluster(TBDeletedCluster);
		model.addAttribute("clusterIp", clusterIp);
		return "provider/cluster-deleted-confirmation";
	}
	
	@RequestMapping(value = "/uploadClusterConfirmation")
	public String uploadNewCluster(@Valid @ModelAttribute("newClusterForm") UploadClusterForm uploadClusterForm,BindingResult theBindingResult, Model model) {
		
		if(theBindingResult.hasErrors()) {
			return "provider/provider-dashboard";
		}
		
		// create new cluster from information in form
		Cluster newCluster = new Cluster(uploadClusterForm.getClusterIp());	

		// get current user 
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		// add new cluster to current user so 
		currentAccount.addCluster(newCluster);
		// push new cluster to cluster table
		if(!clusterService.saveCluster(newCluster)){
			String message = "Invalid IP address. IP address is already in the database";
			model.addAttribute("message",message);
			return "redirect:/provider";
		}
		

		
		model.addAttribute("clusterIp", uploadClusterForm.getClusterIp());
		return "provider/cluster-upload-confirmation";
	}
}
