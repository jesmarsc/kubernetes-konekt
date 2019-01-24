package com.kubernetes.konekt.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.form.UploadClusterForm;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;


@Controller
public class ProviderController {
	
	@Autowired
	private AccountService accountService;

	@Autowired 
	private ClusterService clusterService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@RequestMapping(value = "/provider")
	public String showProviderDashboard(@Valid @ModelAttribute("newClusterForm") UploadClusterForm uploadClusterForm, 
			BindingResult theBindingResult, Model model) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);
		
		UploadClusterForm newClusterForm = new UploadClusterForm();
		model.addAttribute("newClusterForm", newClusterForm);

		return "provider/provider-dashboard";
	}

	@RequestMapping(value = "/provider/delete{clusterUrl}")
	public String deleteCluster(@RequestParam("clusterUrl") String clusterUrl, Model model) {

		Cluster TBDeletedCluster = clusterService.getCluster(clusterUrl);
		
		// TODO: CLEANUP DEPLOYMENTS
		
		clusterService.deleteCluster(TBDeletedCluster);
		
		// TODO: HANDLE IF NOT SUCCESSFUL
		
		String deleteClusterSuccessStatus = "Deleted Cluster Success: ";
		String deleteClusterSuccessMessage = "Cluster with URL: " + TBDeletedCluster.getClusterUrl() + " has been deleted";
		
		model.addAttribute("deleteClusterSuccessMessage", deleteClusterSuccessMessage);
		model.addAttribute("deleteClusterSuccessStatus", deleteClusterSuccessStatus);
		
		return this.showProviderDashboard(null, null, model);
	}
	
	@RequestMapping(value = "/provider/upload")
	public String uploadCluster(@Valid @ModelAttribute("newClusterForm") UploadClusterForm uploadClusterForm,
			BindingResult theBindingResult, Model model) {

		if(theBindingResult.hasErrors()) {
			String uploadClusterFailStatus = "Cluster Upload Failed";
			
			model.addAttribute("uploadClusterFailStatus", uploadClusterFailStatus);
			return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
		}
		
		try {
			String clusterUrl = uploadClusterForm.getClusterUrl();
			String clusterUsername = uploadClusterForm.getClusterUsername();
			String clusterPassword = uploadClusterForm.getClusterPassword();
			
			Cluster newCluster = new Cluster(clusterUrl, clusterUsername, clusterPassword);	
	
			// Get current user 
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Account currentAccount = accountService.findByUserName(username);
			
			// Add new cluster to current user
			currentAccount.addCluster(newCluster);
			
			// Update database to persist changes
			accountService.updateAccountTables(currentAccount);
			String uploadClusterSuccessStatus = "Cluster Upload Success:";
			String uploadClusterSuccessMessage = "Cluster with URL: "+ newCluster.getClusterUrl() + " has been successfully uploaded";
			
			model.addAttribute("uploadClusterSuccessStatus", uploadClusterSuccessStatus);
			model.addAttribute("uploadClusterSuccessMessage", uploadClusterSuccessMessage);
			
			return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
			
		} catch(Exception e) {
			System.out.println( "\n" + e + "\n");
			String uploadClusterFailStatus = "Cluster Upload Failed:";
			String uploadClusterFailMessage= "The URL entered is already registered to another cluster";
			model.addAttribute("uploadClusterFailStatus", uploadClusterFailStatus);
			model.addAttribute("uploadClusterFailMessage", uploadClusterFailMessage);
			return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
		}
	}
	
}
