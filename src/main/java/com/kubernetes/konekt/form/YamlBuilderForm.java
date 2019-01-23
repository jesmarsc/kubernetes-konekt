package com.kubernetes.konekt.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class YamlBuilderForm {
	@NotNull(message = "Required Field")
	@Size(min = 3, message = "Required Field")
	private String deploymentName;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	private String image;
	
	@NotNull(message = "Required Field")
	private String clusterUrl;
	
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="msg")
	@NotNull(message="Required Field")
	private String replicas;	// add to jsp
	
	
	@Size(min=1, message = "Required Field")
	private List<String> key;
	
	@Size(min=1, message = "Required Field")
	private List<String> value;


	@NotNull(message="Required Field")
	@Size(min=1, message="Required Field")
	private String containerPort;


	
	public YamlBuilderForm() {

		this.replicas = "1";
		this.containerPort = "80";
		
	
		
	}



	public String getClusterUrl() {
		return clusterUrl;
	}



	public void setClusterUrl(String clusterUrl) {
		this.clusterUrl = clusterUrl;
	}



	public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}



	public String getReplicas() {
		return replicas;
	}



	public void setReplicas(String replicas) {
		this.replicas = replicas;
	}



	public String getDeploymentName() {
		return deploymentName;
	}



	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}

	public List<String> getKey() {
		return key;
	}

	public boolean setKey(String key) {
		if(key.isEmpty())
			return false;
		if(this.key == null) {
			this.key = new ArrayList<String>();
		}
		this.key.add(key);
		return true;
	}


	public void setKey(List<String> key) {
		this.key = key;
	}



	public List<String> getValue() {
		return value;
	}



	public boolean setValue(String value) {
		if(value.isEmpty())
			return false;
		
		if(this.value == null) {
			this.value  = new ArrayList<String>();
		}
		this.value.add(value);
		return true;
	}


	
	
	public void setValue(List<String> value) {
		this.value = value;
	}
	


	public String getContainerPort() {
		return containerPort;
	}



	public void setContainerPort(String containerPort) {
		this.containerPort = containerPort;
	}

	
}
