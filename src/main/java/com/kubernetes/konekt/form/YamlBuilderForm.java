package com.kubernetes.konekt.form;

import javax.validation.constraints.NotBlank;
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
	
	@Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="msg")
	@NotNull(message="Required Field")
	private String replicas;	
	
	
	@NotBlank(message = "Required Field")
	private String key;
	
	@NotBlank(message = "Required Field")
	private String value;


	@NotNull(message="Required Field")
	@Size(min=1, message="Required Field")
	private String containerPort;


	
	public YamlBuilderForm() {

		this.replicas = "1";
		this.containerPort = "80";
		this.key = "app";
		
	
		
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



	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getContainerPort() {
		return containerPort;
	}



	public void setContainerPort(String containerPort) {
		this.containerPort = containerPort;
	}

	
}
