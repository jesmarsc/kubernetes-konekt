package com.kubernetes.konekt.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kubernetes.konekt.client.ClusterApi;
import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.entity.Container;
import com.kubernetes.konekt.form.UploadContainerToClusterForm;
import com.kubernetes.konekt.form.YamlBuilderForm;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;
import com.kubernetes.konekt.service.ContainerService;

import io.kubernetes.client.ApiException;

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
	
	@RequestMapping(value = "/user/build-yaml")
	public String yamlBuilder( @ModelAttribute("YamlBuilderForm") YamlBuilderForm yamlBuildForm, Model model) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);
		List<Cluster> availableClusters = clusterService.getAllClusters();
		model.addAttribute("availableClusters", availableClusters);
		return "user/yaml-builder-form";
	}

	@RequestMapping(value = "/user/YamlBuildConfirmation")
	public String yamlBuilderConfirmation(@Valid @ModelAttribute("YamlBuilderForm") YamlBuilderForm yamlBuildForm,
			BindingResult theBindingResult, Model model) {

		if (theBindingResult.hasErrors()) {

			return yamlBuilder(yamlBuildForm,model);
		}
		// getting username to retrieve account and to use as namespace.
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		String clusterUrl = yamlBuildForm.getClusterUrl();
		Cluster cluster = clusterService.getCluster(clusterUrl);
		String clusterUser = cluster.getClusterUsername();
		String clusterPass = cluster.getClusterPassword();
		List<String> deploymentNames = null;

		// check if namespace already exist
		Boolean doesExist = clusterApi.checkNamespaceAlreadyExist(username, clusterUrl, clusterUser, clusterPass);
		// if namespace does not exist create it
		if (!doesExist) {
			clusterApi.createNamespace(username, clusterUrl, clusterUser, clusterPass);
		}

		try {
			deploymentNames = clusterApi.deploymentFromUserInput(clusterUrl, clusterUser, clusterPass, username,
					yamlBuildForm);
		} catch (IOException | ApiException e) {
			e.printStackTrace();
			String uploadContainerFailStatus = "Deployment Failed";
			String uploadContainerFailMessage = "The YAML: '" + yamlBuildForm.getDeploymentName()
					+ "' could not be uploaded. There was an error uploading the file content";
			model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
			model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
			return this.showUserDashboard(model);
		}

		if (deploymentNames.isEmpty()) {
			String uploadContainerFailStatus = "Deployment Failed";
			String uploadContainerFailMessage = "The YAML: '" + yamlBuildForm.getDeploymentName()
					+ "' could not be uploaded. There was a conflict with currently uploaded deployments. Check metadata (apps may not have the same name)";
			model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
			model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
			return this.showUserDashboard(model);
		}

		for (String name : deploymentNames) {
			Container newContainer = new Container(name, "Running", clusterUrl);
			currentAccount.addContainer(newContainer);
			containerService.saveContainer(newContainer);
			accountService.updateAccountTables(currentAccount);
		}

		String uploadContainerSuccessStatus = "Deployment Succesful";
		String uploadContainerSuccessMessage = "You successfully deployed: '" + yamlBuildForm.getDeploymentName() + "'";
		model.addAttribute("uploadContainerSuccessStatus", uploadContainerSuccessStatus);
		model.addAttribute("uploadContainerSuccessMessage", uploadContainerSuccessMessage);

		return this.showUserDashboard(model);
	}

	@RequestMapping(value = "/user/upload")
	public String uploadContainer(@RequestParam("containerFile") MultipartFile file, 
			@ModelAttribute("uploadForm") UploadContainerToClusterForm uploadForm, Model model) throws ApiException {
		
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
		String clusterUser = cluster.getClusterUsername();
		String clusterPass = cluster.getClusterPassword();
		List<String> deploymentNames = null;

		// check if namespace already exist 
		Boolean doesExist = clusterApi.checkNamespaceAlreadyExist(username, clusterUrl, clusterUser, clusterPass);
		// if namespace does not exist create it
		if(!doesExist) {
			clusterApi.createNamespace(username,clusterUrl, clusterUser, clusterPass);
		}


		try {
			deploymentNames = clusterApi.parseYaml(file, clusterUrl, clusterUser, clusterPass, username);
		} catch (IOException | ApiException e) { 
            e.printStackTrace();
			String uploadContainerFailStatus = "Deployment Failed";
			String uploadContainerFailMessage =  "The YAML: '" + file.getOriginalFilename() + "' could not be uploaded. There was an error uploading the file content";
			model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
			model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }
		
		if(deploymentNames.isEmpty()) {
			String uploadContainerFailStatus = "Deployment Failed";
			String uploadContainerFailMessage =  "The YAML: '" + file.getOriginalFilename() + "' could not be uploaded. There was a conflict with currently uploaded deployments. Check metadata (apps may not have the same name)";
			model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
			model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
		}
		
		for(String name : deploymentNames) {
			Container newContainer = new Container(name, "Running", clusterUrl);
			currentAccount.addContainer(newContainer);
			containerService.saveContainer(newContainer);
			accountService.updateAccountTables(currentAccount);
		}
		
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
		Cluster cluster = clusterService.getCluster(clusterUrl);

		String userName = cluster.getClusterUsername();
		String passWord = cluster.getClusterPassword();
		clusterApi.deleteDeployment(deploymentName, username, clusterUrl, userName, passWord);
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
