package com.kubernetes.konekt.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class YamlBuilderForm {
	@NotNull(message = "Required Field")
	@Size(min = 3, message = "Required Field")
	private String deploymentName;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	private String applicationName;
	
	
	private String version;		
	

	private List<String> key;
	
	
	private List<String> value;
	
	@NotNull(message="Required Field")
	@Size(min=1, message="Required Field")
	private String containerPort;

	
	
	public YamlBuilderForm() {
		this.version = "latest";
		
	
		
	}



	public String getDeploymentName() {
		return deploymentName;
	}



	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}



	public String getApplicationName() {
		return applicationName;
	}



	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}



	public String getVersion() {
		return version;
	}



	public void setVersion(String version) {
		this.version = version;
	}



	public List<String> getKey() {
		return key;
	}



	public void setKey(String key) {
		if(this.key == null) {
			this.key = new ArrayList<String>();
		}
		this.key.add(key);
	}



	public List<String> getValue() {
		return value;
	}



	public void setValue(String value) {
		if(this.value == null) {
			this.value  = new ArrayList<String>();
		}
		this.value.add(value);
	}



	public String getContainerPort() {
		return containerPort;
	}



	public void setContainerPort(String containerPort) {
		this.containerPort = containerPort;
	}

	
}
