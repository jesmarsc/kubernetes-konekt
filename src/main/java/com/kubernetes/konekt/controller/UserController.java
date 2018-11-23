package com.kubernetes.konekt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

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
	AccountService accountService;
	
	@Autowired 
	ClusterService clusterService;
	
	@Autowired
	ContainerService containerService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
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
	
	@RequestMapping(value = "/uploadContainerToClusterConfirmation")
	public String uploadContainerToCluster(@Valid @ModelAttribute("uploadForm") UploadContainerToClusterForm uploadForm, 
			BindingResult theBindingResult, Model model) {

		if(theBindingResult.hasErrors()) {
			return this.showUserDashboard(model);
		}

		String uploadContainerToClusterSuccessStatus = "Uploaded Successfully";
		String uploadContainerToClusterSuccessMessage = "You have successfully uploaded " + uploadForm.getContainerName()  + 
				" to cluster with IP address:" +  uploadForm.getClusterIp();
		model.addAttribute("uploadContainerToClusterSuccessStatus", uploadContainerToClusterSuccessStatus);
		model.addAttribute("uploadContainerToClusterSuccessMessage",uploadContainerToClusterSuccessMessage);
		return this.showUserDashboard(model);
	}
	
	@RequestMapping(value = "/deleteContainerConfirmation")
	public String deleteContainer( @RequestParam("containerName")String containerName, Model model) {

		try {
		// get current user 
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		
		// convert account id to string
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("");
		stringBuilder.append(currentAccount.getId());
		String stringId = stringBuilder.toString();
		
		//Path for container to be deleted
	    String UPLOADED_CONTAINER_PATH = "containers/" + stringId + "/" + containerName;
        Path path = Paths.get(UPLOADED_CONTAINER_PATH);
        
		//delete container from container folder add userid to path
		Files.delete(path);
		
		// delete container information from database
		Container containerTBD = containerService.getContainerByContainerPath(UPLOADED_CONTAINER_PATH);
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
	
	@RequestMapping(value = "/uploadContainerConfirmation")
	public String uploadContainer( @RequestParam("containerFile")MultipartFile file, Model model) {
		

	    String containerName = file.getOriginalFilename();

		// file is empty
		if (file.isEmpty()) {

			String uploadContainerFailStatus = "Container Upload Failed";
			String uploadContainerFailMessage = "The container: '" + file.getOriginalFilename() + "' could not be uploaded, chosen file was empty";
            model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
            model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

		try {

            // Get the file user chose and save it somewhere
            byte[] bytes = file.getBytes();
			// get current user 
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Account currentAccount = accountService.findByUserName(username);
			//convert id to string
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("");
			stringBuilder.append(currentAccount.getId());
			String stringId = stringBuilder.toString();
			//Directories in path
			String UPLOADED_CONTAINER_DIR = "containers/" + stringId + "/";
			//Save the uploaded file to this folder
    	    String UPLOADED_CONTAINER_PATH = "containers/" + stringId + "/" + file.getOriginalFilename();
    	    //check if container is already on database before you write the the container to containers folder
            if(containerService.containerExists(UPLOADED_CONTAINER_PATH)) {

    			String uploadContainerFailStatus = "Container Upload Failed";
    			String uploadContainerFailMessage = "The container: '" + file.getOriginalFilename() + "' could not be uploaded because container with that name already exist";
				model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
                model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
                return this.showUserDashboard(model);
                
            }
            // get path object
			Path createDirs = Paths.get(UPLOADED_CONTAINER_DIR);
			// create any missing directories
			Files.createDirectories(createDirs);
    	    //create path of where container will be saved
            Path path = Paths.get(UPLOADED_CONTAINER_PATH);
            // write container content to user folder
            Files.write(path, bytes);
			// create container object
			Container newContainer = new Container(containerName,UPLOADED_CONTAINER_PATH);
			// add container object to container list of currentAccount
			currentAccount.addContainer(newContainer);
			
			
			// sync database with program. add container to database
			accountService.updateAccountTables(currentAccount);

        } catch (IOException e) {
        	
            e.printStackTrace();
            
			String uploadContainerFailStatus = "Container Upload Failed";
			String uploadContainerFailMessage =  "The container: '" + file.getOriginalFilename() + "' could not be uploaded. There was an error uploading the file content";
			model.addAttribute("uploadContainerFailStatus", uploadContainerFailStatus);
			model.addAttribute("uploadContainerFailMessage", uploadContainerFailMessage);
            return this.showUserDashboard(model);
        }

		String uploadContainerSuccessStatus = "Container Uploaded Succesfully";
		String uploadContainerSuccessMessage = "You successfully uploaded container: '" + file.getOriginalFilename() + "'";
		model.addAttribute("uploadContainerSuccessStatus", uploadContainerSuccessStatus);
		model.addAttribute("uploadContainerSuccessMessage", uploadContainerSuccessMessage);
		
		return this.showUserDashboard(model);
	}
	
	

}
