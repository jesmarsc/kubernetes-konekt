package com.kubernetes.konekt.controller;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubernetes.konekt.client.ClusterApi;
import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.entity.Container;
import com.kubernetes.konekt.form.UploadClusterForm;
import com.kubernetes.konekt.metric.Metric;
import com.kubernetes.konekt.metric.Prometheus;
import com.kubernetes.konekt.security.ClusterSecurity;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;
import com.kubernetes.konekt.service.ContainerService;

import io.kubernetes.client.ApiException;


@Controller
public class ProviderController {
	
	@Autowired
	private AccountService accountService;

	@Autowired 
	private ClusterService clusterService;
	
	@Autowired 
	private ContainerService containerService;
	
	@Autowired
	private ClusterSecurity clusterSecurity;
	
	@Autowired
	private Prometheus prometheus;
	
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
		
		List<Container> containers = containerService.getContainersByProviderId(currentAccount.getId());
		UploadClusterForm newClusterForm = new UploadClusterForm();
		
		model.addAttribute("currentAccount", currentAccount);
		model.addAttribute("runningContainers", containers);
		model.addAttribute("newClusterForm", newClusterForm);
		
		List<Cluster> clusters = currentAccount.getClusters();
		List<Metric> metrics = new ArrayList<Metric>();
		for(Cluster cluster:clusters) {
		    try {
                metrics.add(prometheus.getUsageMetric(cluster.getClusterUrl().substring(8)));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}

		model.addAttribute("metrics", metrics);

		return "provider/provider-dashboard";
	}

	@RequestMapping(value = "/provider/delete{clusterUrl}")
	public String deleteCluster(@RequestParam("clusterUrl") String clusterUrl, Model model) throws IOException, ApiException {

		Cluster deleteCluster = clusterService.getCluster(clusterUrl);
		Blob encryptedUsername = deleteCluster.getEncryptedUsername();
		Blob encryptedPassword = deleteCluster.getEncryptedUsername();
		String clusterUser = clusterSecurity.decodeCredential(encryptedUsername);
		String clusterPass = clusterSecurity.decodeCredential(encryptedPassword);
	

		// get list of users who have deployments on cluster
		List<Container> containers = containerService.getContainerByClusterUrl(clusterUrl);
		// delete deployments from cluster
		ClusterApi clusterApi = new ClusterApi(clusterUrl, clusterUser, clusterPass);
		for(Container container : containers) {
			String deploymentName = container.getContainerName();
			String namespace = container.getAccount().getUserName();
			try {
				clusterApi.deleteDeployment(namespace, deploymentName);
				containerService.deleteContainer(container);
				accountService.updateAccountTables(container.getAccount());
			} catch(ApiException e) {
				e.printStackTrace();
				String deleteClusterSuccessMessage = "Delete Cluster Failed: ";
				String deleteClusterSuccessStatus = "Cluster with URL: " + deleteCluster.getClusterUrl() + " was NOT deleted. There was a problem removing deployments from cluster";
				
				model.addAttribute("deleteClusterSuccessMessage", deleteClusterSuccessMessage);
				model.addAttribute("deleteClusterSuccessStatus", deleteClusterSuccessStatus);
			}
		}
		
		clusterService.deleteCluster(deleteCluster);
		
		prometheus.removeCluster(clusterUrl.substring(8));
		
		String deleteClusterSuccessStatus = "Deleted Cluster Success: ";
		String deleteClusterSuccessMessage = "Cluster with URL: " + deleteCluster.getClusterUrl() + " has been deleted";
		
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
			String clusterUser = uploadClusterForm.getClusterUsername();
			String clusterPass = uploadClusterForm.getClusterPassword();
			Blob encryptedUsername = clusterSecurity.encodeCredential(clusterUser);
			Blob encryptedPassword = clusterSecurity.encodeCredential(clusterPass);
			
			Cluster newCluster = new Cluster(clusterUrl, clusterUser, clusterPass, encryptedUsername, encryptedPassword, 0);
	
			// Get current user 
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Account currentAccount = accountService.findByUserName(username);
			
			// Add new cluster to current user
			currentAccount.addCluster(newCluster);
			
			// Update database to persist changes
			accountService.updateAccountTables(currentAccount);
			
			// Set up prometheus
			ClusterApi clusterApi = new ClusterApi(clusterUrl, clusterUser, clusterPass);
			clusterApi.setupPrometheus(currentAccount.getId(), clusterUrl, clusterUser, clusterPass);
			
			
			String uploadClusterSuccessStatus = "Cluster Upload Success:";
			String uploadClusterSuccessMessage = "Cluster with URL: "+ newCluster.getClusterUrl() + " has been successfully uploaded";
			
			model.addAttribute("uploadClusterSuccessStatus", uploadClusterSuccessStatus);
			model.addAttribute("uploadClusterSuccessMessage", uploadClusterSuccessMessage);
			
			return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
			
		} catch(Exception e) {
			e.printStackTrace();
			String uploadClusterFailStatus = "Cluster Upload Failed:";
			String uploadClusterFailMessage= "The URL entered is already registered to another cluster";
			model.addAttribute("uploadClusterFailStatus", uploadClusterFailStatus);
			model.addAttribute("uploadClusterFailMessage", uploadClusterFailMessage);
			return this.showProviderDashboard(uploadClusterForm, theBindingResult, model);
		}
	}
	
	@RequestMapping(value = "/provider/delete-container")
	public String deleteContainer(@RequestParam("containerId") Long id, Model model) {

		Container containerTBD = containerService.getContainerById(id);
		String containerName = containerTBD.getContainerName();
		
		try {
			// get username of user who owns container
			String username = containerTBD.getAccount().getUserName();
			// Deleting Deployment from cluster
			String deploymentName = containerTBD.getContainerName();
			String clusterUrl = containerTBD.getClusterUrl();
			Cluster cluster = clusterService.getCluster(clusterUrl);
			Blob encryptedUsername =cluster.getEncryptedUsername();
			Blob encryptedPassword = cluster.getEncryptedPassword();
			String clusterUser = clusterSecurity.decodeCredential(encryptedUsername);
			String clusterPass = clusterSecurity.decodeCredential(encryptedPassword);
			ClusterApi clusterApi = new ClusterApi(clusterUrl, clusterUser, clusterPass);
			clusterApi.deleteDeployment(username, deploymentName);
			// Deleting Deployment from database
			containerService.deleteContainer(containerTBD);
		} catch (Exception e) {
			e.printStackTrace();
			String deleteContainerToClusterStatus = "Container Delete Failed";
			String deleteContainerToClusterMessage = "The container: '" + containerName
					+ "' could not be deleted. Container not found in system";
			model.addAttribute("deleteContainerToClusterStatus", deleteContainerToClusterStatus);
			model.addAttribute("deleteContainerToClusterMessage", deleteContainerToClusterMessage);
			return this.showProviderDashboard(null, null, model);
		}

		String deleteContainerToClusterStatus = "Container Deleted Successfully";
		String deleteContainerToClusterMessage = "The container: '" + containerName
				+ "' was successfully deleted from system ";
		model.addAttribute("deleteContainerToClusterStatus", deleteContainerToClusterStatus);
		model.addAttribute("deleteContainerToClusterMessage", deleteContainerToClusterMessage);
		return this.showProviderDashboard(null, null, model);

	}
	
}
