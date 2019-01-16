package com.kubernetes.konekt.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kubernetes.konekt.client.ClusterApi;
import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.entity.Container;
import com.kubernetes.konekt.form.UploadContainerToClusterForm;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;
import com.kubernetes.konekt.service.ContainerService;

@Controller
public class UserController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired 
	private ClusterService clusterService;

	@Autowired
	private ContainerService containerService;
	
	@Autowired
	private ClusterApi clusterApi;
	
	@RequestMapping(value = "/user")
	public String showUserDashboard(Model model) {
		
		UploadContainerToClusterForm uploadContainerClusterForm = new UploadContainerToClusterForm();
		model.addAttribute("uploadContainerClusterForm", uploadContainerClusterForm);
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);
		
		List<Cluster> availableClusters = clusterService.getAllClusters();
		model.addAttribute("availableClusters", availableClusters);
		
		return "user/user-dashboard";
	}
	
	@RequestMapping(value = "/user/upload")
	public String uploadContainer(@RequestParam("containerFile") MultipartFile file, 
			@ModelAttribute("uploadForm") UploadContainerToClusterForm uploadForm, Model model) {
		
		if (file.isEmpty()) {
			String uploadContainerFailStatus = "Container Upload Failed";
			String uploadContainerFailMessage = "The YAML: '" + file.getOriginalFilename() + "' could not be uploaded, chosen file was empty";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }
		
 
		// getting username to retrieve account and to use as namespace.
		String username = SecurityContextHolder.getContext().getAuthentication().getName(); 
		Account currentAccount = accountService.findByUserName(username);
		String clusterUrl = uploadForm.getClusterUrl();
		Cluster cluster = clusterService.getCluster(clusterUrl);
		String userName = cluster.getClusterUsername();
		String passWord = cluster.getClusterPassword();
		String deployment = null;

		// check if namespace already exist 
		Boolean doesExist = clusterApi.CheckNamespaceAlreadyExist(username,clusterUrl, userName, passWord);
		// if namespace does not exist create it
		if(!doesExist) {
			clusterApi.createNamespace(username,clusterUrl, userName, passWord);

		}


		try {
			deployment = clusterApi.execYaml(file, clusterUrl, userName, passWord, username);
		} catch (IOException e) { 
            e.printStackTrace();
			String uploadContainerFailStatus = "Deployment Failed";
			String uploadContainerFailMessage =  "The YAML: '" + file.getOriginalFilename() + "' could not be uploaded. There was an error uploading the file content";
			model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
			model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }
		
		if(deployment == null) {
			String uploadContainerFailStatus = "Deployment Failed";
			String uploadContainerFailMessage =  "The YAML: '" + file.getOriginalFilename() + "' could not be uploaded. There was a conflict with currently uploaded deployments. Check metadata (apps may not have the same name)";
			model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
			model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
		}
		
		Container newContainer = new Container(deployment, "Running", clusterUrl);
		
		currentAccount.addContainer(newContainer);
		containerService.saveContainer(newContainer);
		accountService.updateAccountTables(currentAccount);
		
		String uploadContainerSuccessStatus = "Deployment Succesful";
		String uploadContainerSuccessMessage = "You successfully deployed: '" + file.getOriginalFilename() + "'";
		model.addAttribute("uploadContainerSuccessStatus", uploadContainerSuccessStatus);
		model.addAttribute("uploadContainerSuccessMessage", uploadContainerSuccessMessage);
		
		return this.showUserDashboard(model);
	}
	
	
	@RequestMapping(value = "/user/delete")
	public String deleteContainer( @RequestParam("containerId")Long id, Model model) {
		
		Container containerTBD = containerService.getContainerById(id);
		String containerName = containerTBD.getContainerName();
		try {
		// get current user 
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);

		// Deleting Deployment from cluster
		String deploymentName = containerTBD.getContainerName();
		String clusterUrl = containerTBD.getClusterUrl();
		System.out.println("\n\n\n\n\n\n\n\n\n " + clusterUrl + "\n\n\n\n\n\n\n" );
		Cluster cluster = clusterService.getCluster(clusterUrl);

		String userName = cluster.getClusterUsername();
		String passWord = cluster.getClusterPassword();
		clusterApi.deleteDeployment(deploymentName, clusterUrl, userName, passWord, username);
		// Deleting Deployment from database 
		containerService.deleteContainer(containerTBD);
		}
		catch(Exception e) {
			e.printStackTrace();
			String deleteContainerToClusterStatus = "Container Delete Failed";
			String deleteContainerToClusterMessage = "The container: '" + containerName + "' could not be deleted. Container not found in system";
            model.addAttribute("deleteContainerToClusterStatus", deleteContainerToClusterStatus);
            model.addAttribute("deleteContainerToClusterMessage", deleteContainerToClusterMessage);
            return this.showUserDashboard(model);
		}
		
		
		String deleteContainerToClusterStatus = "Container Deleted Successfully";
		String deleteContainerToClusterMessage = "The container: '" + containerName + "' was successfully deleted from system ";
        model.addAttribute("deleteContainerToClusterStatus", deleteContainerToClusterStatus);
        model.addAttribute("deleteContainerToClusterMessage", deleteContainerToClusterMessage);
		return this.showUserDashboard(model);
		
	}

	
}
