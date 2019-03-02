package com.kubernetes.konekt.controller;

import java.io.IOException;
import java.sql.Blob;
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
import com.kubernetes.konekt.security.ClusterSecurity;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;
import com.kubernetes.konekt.service.ContainerService;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Service;

@Controller
public class UserController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private RoundRobinScheduler scheduler;

    @Autowired
    private ClusterSecurity clusterSecurity;

    @RequestMapping(value = "/user")
    public String showUserDashboard(Model model) {

        UploadContainerToClusterForm uploadContainerClusterForm = new UploadContainerToClusterForm();
        model.addAttribute("uploadContainerClusterForm", uploadContainerClusterForm);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountService.findByUserName(username);
        // check if expected workload is still running
        // Currently any entry on database not found on cluster is removed from database
        new ClusterApi().checkUserWorkload(currentAccount.getContainers());
        model.addAttribute("currentAccount", currentAccount);

        List<Cluster> availableClusters = clusterService.getAllClusters();
        model.addAttribute("availableClusters", availableClusters);
      
        return "user/user-dashboard";
    }

    @RequestMapping(value = "/user/build-yaml")
    public String yamlBuilder(@ModelAttribute("YamlBuilderForm") YamlBuilderForm yamlBuildForm, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountService.findByUserName(username);
        model.addAttribute("currentAccount", currentAccount);

		    return "user/yaml-builder-form";
	  }
  
    @RequestMapping(value = "/user/get-status")
    public String getWorkloadStatus(@RequestParam("containerId") Long id, Model model) {
      // get account
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      Account currentAccount = accountService.findByUserName(username);
      model.addAttribute("currentAccount", currentAccount);
      // get workload that user wants status on
      Container container = containerService.getContainerById(id);
      //get cluster info
      Cluster cluster = clusterService.getCluster(container.getClusterUrl());
      String clusterUrl = cluster.getClusterUrl();
      String clusterUsername = clusterSecurity.decodeCredential(cluster.getEncryptedUsername());
      String clusterPassword = clusterSecurity.decodeCredential(cluster.getEncryptedPassword());
      // set up client
      ClusterApi clusterApi = new ClusterApi();
      clusterApi.setupClient(clusterUrl, clusterUsername, clusterPassword);
      //request update
      try {
        String result = clusterApi.getStatusByKindAndUid(username, container.getKind(), container.getUid());
        System.out.println(result);
        model.addAttribute("statusResult", result);
      } catch (ApiException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
		model.addAttribute("currentAccount", currentAccount);

		return "user/get-status";
	}
    @RequestMapping(value = "/user/YamlBuildConfirmation")
    public String yamlBuilderConfirmation(@Valid @ModelAttribute("YamlBuilderForm") YamlBuilderForm yamlBuildForm,
            BindingResult theBindingResult, Model model)  {
      
        if (theBindingResult.hasErrors()) {
            return yamlBuilder(yamlBuildForm, model);
        }

        // getting username to retrieve account and to use as namespace.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountService.findByUserName(username);
        // round robin scheduler to select cluster
        Cluster chosenCluster = scheduler.getNextCluster();
        Long providerId = chosenCluster.getAccount().getId();
        String clusterUrl = chosenCluster.getClusterUrl();
        Blob encryptedUsername = chosenCluster.getEncryptedUsername();
        Blob encryptedPassword = chosenCluster.getEncryptedPassword();
        String clusterUser = clusterSecurity.decodeCredential(encryptedUsername);
        String clusterPass = clusterSecurity.decodeCredential(encryptedPassword);

        ClusterApi clusterApi = new ClusterApi(clusterUrl, clusterUser, clusterPass);

        try {
            // check if namespace already exist
            Boolean doesExist = clusterApi.checkNamespaceAlreadyExist(username);
            // if namespace does not exist create it
            if (!doesExist) {
                clusterApi.createNamespace(username);
            }
        }catch(Exception e) {
            e.printStackTrace();
            String uploadContainerFailStatus = "Deployment Failed";
            String uploadContainerFailMessage = "The YAML: '" + yamlBuildForm.getDeploymentName()
            + "' could not be uploaded. There was an error accessing the selected cluster. Please choose another cluster.";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

        List<Container> resources = new ArrayList<Container>();

        try {
            resources = clusterApi.deploymentFromUserInput(username, yamlBuildForm, providerId);
        } catch (IOException | ApiException e) {
            e.printStackTrace();
            String uploadContainerFailStatus = "Deployment Failed";
            String uploadContainerFailMessage = "The YAML: '" + yamlBuildForm.getDeploymentName()
            + "' could not be uploaded. There was an error uploading the file content. Error Message: " + e.getMessage();;
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

        if (resources.isEmpty()) {
            String uploadContainerFailStatus = "Deployment Failed";
            String uploadContainerFailMessage = "The YAML: '" + yamlBuildForm.getDeploymentName()
            + "' could not be uploaded. There was a conflict with currently uploaded deployments. Check metadata (apps may not have the same name)";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

        for (Container resource : resources) {
            currentAccount.addContainer(resource);
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
            @ModelAttribute("uploadForm") UploadContainerToClusterForm uploadForm, Model model) {

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
        Cluster chosenCluster;
        // Choose cluster for the user.
        if(uploadForm.getClusterUrl().isEmpty()) {
            chosenCluster = scheduler.getNextCluster();
        } else {
            chosenCluster = clusterService.getCluster(uploadForm.getClusterUrl());
        }

        // Get url, username, and password needed to access cluster.
        Long providerId = chosenCluster.getAccount().getId();
        String clusterUrl = chosenCluster.getClusterUrl();
        Blob encryptedUsername = chosenCluster.getEncryptedUsername();
        Blob encryptedPassword = chosenCluster.getEncryptedPassword();
        String clusterUser = clusterSecurity.decodeCredential(encryptedUsername);
        String clusterPass = clusterSecurity.decodeCredential(encryptedPassword);

        ClusterApi clusterApi = new ClusterApi(clusterUrl, clusterUser, clusterPass);

        // Check if namespace already exist.
        Boolean doesExist;
        try {
            doesExist = clusterApi.checkNamespaceAlreadyExist(username);
            // If namespace does not exist, create it.
            if (!doesExist) {
                clusterApi.createNamespace(username);
            }
        } catch (ApiException e1) {
            String uploadContainerFailStatus = "Upload Failed";
            String uploadContainerFailMessage = "The YAML: '" + file.getOriginalFilename()
            + "' could not be uploaded. Error Message: " + e1.getMessage();
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            e1.printStackTrace();
            return this.showUserDashboard(model);
        }

        List<Container> resources = new ArrayList<Container>();

        try {
            resources = clusterApi.parseYaml(clusterApi.saveFileLocally(file), username, providerId);
        } catch (IOException e) {
            e.printStackTrace();
            String uploadContainerFailStatus = "Upload Failed";
            String uploadContainerFailMessage = "The YAML: '" + file.getOriginalFilename()
            + "' could not be uploaded. There was an error uploading the file content.";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        } 
        catch (ApiException e) {
            e.printStackTrace();

            String uploadContainerFailStatus = "Upload Failed";
            String uploadContainerFailMessage = "The YAML: '" + file.getOriginalFilename()
            + "' could not be uploaded. There was a conflict with currently uploaded deployments. Error Message: " + e.getMessage();
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

        for (Container resource : resources) {
            currentAccount.addContainer(resource);
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
        Blob encryptedUsername = cluster.getEncryptedUsername();
        Blob ecnryptedPassword = cluster.getEncryptedPassword();
        String clusterUser = clusterSecurity.decodeCredential(encryptedUsername);
        String clusterPass = clusterSecurity.decodeCredential(ecnryptedPassword);

        ClusterApi clusterApi = new ClusterApi(clusterUrl, clusterUser, clusterPass);

        try {
            if (kind.equals("Deployment")) {
                clusterApi.deleteDeployment(username, deploymentName);
            } else if (kind.equals("Service")) {
                clusterApi.deleteService(username, deploymentName);
            } else if (kind.equals("ConfigMap")) {
                clusterApi.deleteConfigMap(username, deploymentName);
            }
            // Deleting Deployment from database
            containerService.deleteContainer(containerTBD);
        } catch (ApiException e) {
            e.printStackTrace();
            String deleteContainerToClusterStatus = "Delete Failed";
            String deleteContainerToClusterMessage = "The deployment: '" + deploymentName + "' could not be deleted. Error Message: " + e.getMessage();
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
