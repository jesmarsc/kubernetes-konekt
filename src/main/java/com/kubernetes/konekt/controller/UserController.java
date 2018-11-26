package com.kubernetes.konekt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	private AccountService accountService;
	@Autowired 
	private ClusterService clusterService;

	@Autowired
	private ContainerService containerService;
	
	@RequestMapping(value = "/user")
	public String showUserDashboard(Model model) {
		
		UploadContainerToClusterForm uploadContainerClusterForm = new UploadContainerToClusterForm();
		model.addAttribute("uploadContainerClusterForm", uploadContainerClusterForm);
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		model.addAttribute("currentAccount", currentAccount);
		
		List<Cluster> availableClusters = clusterService.getAllAvailableClusters();
		model.addAttribute("availableClusters", availableClusters);
		
		return "user/user-dashboard";
	}
	
	@RequestMapping(value = "/uploadContainerToClusterConfirmation")
	public String uploadContainerToCluster(@ModelAttribute("uploadForm") UploadContainerToClusterForm uploadForm, 
			BindingResult theBindingResult, Model model) {

		//if cluster ip or container name fields are empty throw error
		if(uploadForm.getClusterIp() == null || uploadForm.getContainerName() == null) {
 			String uploadContainerToClusterFailStatus = " Upload Failed";
			String uploadContainerToClusterFailMessage = "Cluster or Container was not selected";
			model.addAttribute("uploadContainerToClusterFailStatus", uploadContainerToClusterFailStatus);
			model.addAttribute("uploadContainerToClusterFailMessage",uploadContainerToClusterFailMessage);
			return this.showUserDashboard(model);
		}
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account currentAccount = accountService.findByUserName(username);
		//set container status to running
		Container updateContainer = currentAccount.getContainers().stream()
				  .filter(container -> uploadForm.getContainerName().equals(container.getContainerName()))
				  .findAny()
				  .orElse(null);
		Integer index = currentAccount.getContainers().indexOf(updateContainer);
		updateContainer.setIpAddress(uploadForm.getClusterIp());
		updateContainer.setStatus("Running");
		currentAccount.updateContainer(index, updateContainer);
		
		//get cluster from database
		Cluster updateCluster = clusterService.getCluster(uploadForm.getClusterIp());
		// set cluster status to running 
		updateCluster.setContainerName(uploadForm.getContainerName());
		// set cluster container name
		updateCluster.setStatus("Running");
		// sync with database
		clusterService.updateEntry(updateCluster);
		
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
		// If container was running on cluster remove from cluster and free cluster so another user can use it
		if(!containerTBD.getIpAddress().equals("N/A")) {
			System.out.println("\n\n\n\n\n\n\n" + !containerTBD.getIpAddress().equals("N/A")  +"\n\n\n\n\n\n");
			Cluster updateCluster = clusterService.getCluster(containerTBD.getIpAddress());
			updateCluster.setContainerName("N/A");
			updateCluster.setStatus("Stopped");
			clusterService.updateEntry(updateCluster);
		}
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
            String containerStatus = "Stopped";
            String clusterIp = "N/A";
			Container newContainer = new Container(containerName,UPLOADED_CONTAINER_PATH,containerStatus,clusterIp);
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