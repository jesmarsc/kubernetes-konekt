package com.kubernetes.konekt.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.kubernetes.konekt.scheduler.RoundRobinScheduler;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;
import com.kubernetes.konekt.service.ContainerService;

import io.kubernetes.client.ApiException;
import javafx.util.Pair;

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

	@Autowired
	private RoundRobinScheduler scheduler;

	@RequestMapping(value = "/user")
	public String showUserDashboard(Model model) {

		UploadContainerToClusterForm uploadContainerClusterForm = new UploadContainerToClusterForm();
		model.addAttribute("uploadContainerClusterForm", uploadContainerClusterForm);

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);

		List<Cluster> availableClusters = clusterService.getAllClusters();
		model.addAttribute("availableClusters", availableClusters);

		// TODO:check all containers are still on clusters

		return "user/user-dashboard";
	}

	@RequestMapping(value = "/user/build-yaml")
	public String yamlBuilder(@ModelAttribute("YamlBuilderForm") YamlBuilderForm yamlBuildForm, Model model) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);

		return "user/yaml-builder-form";
	}

    @RequestMapping(value = "/user/YamlBuildConfirmation")
    public String yamlBuilderConfirmation(@Valid @ModelAttribute("YamlBuilderForm") YamlBuilderForm yamlBuildForm,
            BindingResult theBindingResult, Model model) throws ApiException {

        if (theBindingResult.hasErrors()) {
            return yamlBuilder(yamlBuildForm, model);
        }

        // getting username to retrieve account and to use as namespace.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountService.findByUserName(username);
        // round robin scheduler to select cluster
        Cluster chosenCluster = scheduler.getNextCluster();
        String clusterUrl = chosenCluster.getClusterUrl();
        String clusterUser = chosenCluster.getClusterUsername();
        String clusterPass = chosenCluster.getClusterPassword();
        List<Container> deployments = null;

        // check if namespace already exist
        Boolean doesExist = clusterApi.checkNamespaceAlreadyExist(username, clusterUrl, clusterUser, clusterPass);
        // if namespace does not exist create it
        if (!doesExist) {
            clusterApi.createNamespace(username, clusterUrl, clusterUser, clusterPass);
        }

        try {
            deployments = clusterApi.deploymentFromUserInput(clusterUrl, clusterUser, clusterPass, username,
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

        if (deployments.isEmpty()) {
            String uploadContainerFailStatus = "Deployment Failed";
            String uploadContainerFailMessage = "The YAML: '" + yamlBuildForm.getDeploymentName()
                    + "' could not be uploaded. There was a conflict with currently uploaded deployments. Check metadata (apps may not have the same name)";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

        for (Container deployment : deployments) {
            currentAccount.addContainer(deployment);
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
            String uploadContainerFailStatus = "Upload Failed";
            String uploadContainerFailMessage = "The YAML: '" + file.getOriginalFilename()
                    + "' could not be uploaded, the chosen file was empty.";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

        // Get username to retrieve account and to use as namespace.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountService.findByUserName(username);

        // Choose cluster for the user.
        Cluster chosenCluster = scheduler.getNextCluster();

        // Get url, username, and password needed to access cluster.
        String clusterUrl = chosenCluster.getClusterUrl();
        String clusterUser = chosenCluster.getClusterUsername();
        String clusterPass = chosenCluster.getClusterPassword();
        List<Container> deployments = null;

        // Check if namespace already exist.
        Boolean doesExist = clusterApi.checkNamespaceAlreadyExist(username, clusterUrl, clusterUser, clusterPass);

        // If namespace does not exist, create it.
        if (!doesExist) {
            clusterApi.createNamespace(username, clusterUrl, clusterUser, clusterPass);
        }

        try {
            deployments = clusterApi.parseYaml(file, clusterUrl, clusterUser, clusterPass, username);
        } catch (IOException e) {
            e.printStackTrace();
            String uploadContainerFailStatus = "Upload Failed";
            String uploadContainerFailMessage = "The YAML: '" + file.getOriginalFilename()
                    + "' could not be uploaded. There was an error uploading the file content.";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        } catch (ApiException e) {
            e.printStackTrace();
            String uploadContainerFailStatus = "Upload Failed";
            String uploadContainerFailMessage = "The YAML: '" + file.getOriginalFilename()
                    + "' could not be uploaded. There was a conflict with currently uploaded deployments. Check metadata (apps may not have the same name).";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

        for (Container deployment : deployments) {
            currentAccount.addContainer(deployment);
            accountService.updateAccountTables(currentAccount);
        }

        String uploadContainerSuccessStatus = "Upload Succesful";
        String uploadContainerSuccessMessage = "You successfully deployed: '" + file.getOriginalFilename() + "'";
        model.addAttribute("uploadContainerSuccessStatus", uploadContainerSuccessStatus);
        model.addAttribute("uploadContainerSuccessMessage", uploadContainerSuccessMessage);

        return this.showUserDashboard(model);
    }

    @RequestMapping(value = "/user/delete")
    public String deleteContainer(@RequestParam("containerId") Long id, Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Get Deployment
        Container containerTBD = containerService.getContainerById(id);
        String deploymentName = containerTBD.getContainerName();
        String clusterUrl = containerTBD.getClusterUrl();
        String kind = containerTBD.getKind();

        // Get Cluster
        Cluster cluster = clusterService.getCluster(clusterUrl);
        String userName = cluster.getClusterUsername();
        String passWord = cluster.getClusterPassword();

        try {
            if (kind.equals("Deployment")) {
                clusterApi.deleteDeployment(deploymentName, username, clusterUrl, userName, passWord);
            } else if (kind.equals("Service")) {
                clusterApi.deleteService(deploymentName, username, clusterUrl, userName, passWord);
            } else if (kind.equals("ConfigMap")) {
                clusterApi.deleteConfigMap(deploymentName, username, clusterUrl, userName, passWord);
            }
            // Deleting Deployment from database
            containerService.deleteContainer(containerTBD);
        } catch (ApiException e) {
            e.printStackTrace();
            String deleteContainerToClusterStatus = "Delete Failed";
            String deleteContainerToClusterMessage = "The deployment: '" + deploymentName + "' could not be deleted.";
            model.addAttribute("deleteContainerToClusterStatus", deleteContainerToClusterStatus);
            model.addAttribute("deleteContainerToClusterMessage", deleteContainerToClusterMessage);
            return this.showUserDashboard(model);
        }

        String deleteContainerToClusterStatus = "Deleted Successful";
        String deleteContainerToClusterMessage = "The deployment: '" + deploymentName + "' was successfully deleted.";
        model.addAttribute("deleteContainerToClusterStatus", deleteContainerToClusterStatus);
        model.addAttribute("deleteContainerToClusterMessage", deleteContainerToClusterMessage);
        return this.showUserDashboard(model);
    }

}
