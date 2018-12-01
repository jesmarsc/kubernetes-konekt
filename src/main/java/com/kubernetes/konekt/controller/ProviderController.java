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
import com.kubernetes.konekt.entity.Container;
import com.kubernetes.konekt.form.UploadClusterForm;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;
import com.kubernetes.konekt.service.ContainerService;


@Controller
public class ProviderController {
	
	@Autowired
	private AccountService accountService;

	@Autowired 
	private ClusterService clusterService;
	
	@Autowired
	private ContainerService containerService;
	
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

	@RequestMapping(value = "/provider/delete{clusterip}")
	public String deleteCluster(@RequestParam("clusterIp") String clusterIp, Model model) {

		Cluster TBDeletedCluster = clusterService.getCluster(clusterIp);
		//check if cluster was running container if so set container status to stopped
		// 		set container cluster ip to "N/A"
		if((TBDeletedCluster.getContainerName() != null) && !TBDeletedCluster.getContainerName().equals("N/A")) {
			Container updateContainer = containerService.getContainerByContainerIp(clusterIp);
			updateContainer.setIpAddress("N/A");
			updateContainer.setStatus("Stopped");
			containerService.updateEntry(updateContainer);
		}
		
		clusterService.deleteCluster(TBDeletedCluster);
		
		String deleteClusterSuccessStatus = "Deleted Cluster Success: ";
		String deleteClusterSuccessMessage = "Cluster with IP address: " + TBDeletedCluster.getIp() + " has been deleted";
		
		model.addAttribute("deleteClusterSuccessMessage", deleteClusterSuccessMessage);
		model.addAttribute("deleteClusterSuccessStatus", deleteClusterSuccessStatus);
		
		return this.showProviderDashboard(null, null, model);
	}
	
	@RequestMapping(value = "/provider/upload")
	public String uploadCluster(@Valid @ModelAttribute("newClusterForm") UploadClusterForm uploadClusterForm,
			BindingResult theBindingResult, Model model) {

		if(theBindingResult.hasErrors()) {
			String uploadClusterFailStatus = "Cluster Upload Failed:";
			String uploadClusterFailMessage= "IP address enter is invalid";
	
			model.addAttribute("uploadClusterFailStatus", uploadClusterFailStatus);
			model.addAttribute("uploadClusterFailMessage", uploadClusterFailMessage);
			return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
		}
		try {
			// create new cluster from information in form
			String clusterStatus = "Stopped";
			String containerName = "N/A";
			Cluster newCluster = new Cluster(uploadClusterForm.getClusterIp(), clusterStatus, containerName);	
	
			// get current user 
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Account currentAccount = accountService.findByUserName(username);
			// add new cluster to current user so 
			currentAccount.addCluster(newCluster);
			// updates database to sync up with changes
			accountService.updateAccountTables(currentAccount);
			String uploadClusterSuccessStatus ="Cluster Upload Success:";
			String uploadClusterSuccessMessage = "Cluster with IP address: "+newCluster.getIp() + " has been successfully uploaded";
			
			model.addAttribute("uploadClusterSuccessStatus", uploadClusterSuccessStatus);
			model.addAttribute("uploadClusterSuccessMessage", uploadClusterSuccessMessage);
			return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
			}
			catch(Exception e) {
				System.out.println( "\n" + e + "\n");
				String uploadClusterFailStatus = "Cluster Upload Failed:";
				String uploadClusterFailMessage= "IP address enter is already registered to another cluster";
				model.addAttribute("uploadClusterFailStatus", uploadClusterFailStatus);
				model.addAttribute("uploadClusterFailMessage", uploadClusterFailMessage);
				return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
		}
	}
	
}
