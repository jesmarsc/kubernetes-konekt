package com.kubernetes.konekt.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

public class UploadClusterForm {
	
	@URL
	private String clusterUrl;
	
	@NotNull(message="Required Field")
	private String clusterUsername;
	
	@NotNull(message="Required Field")
	private String clusterPassword;
	
	public String getClusterUrl() {
		return clusterUrl;
	}

	public void setClusterUrl(String clusterUrl) {
		this.clusterUrl = clusterUrl;
	}

	public String getClusterUsername() {
		return clusterUsername;
	}

	public void setClusterUsername(String clusterUsername) {
		this.clusterUsername = clusterUsername;
	}

	public String getClusterPassword() {
		return clusterPassword;
	}

	public void setClusterPassword(String clusterPassword) {
		this.clusterPassword = clusterPassword;
	}

	public UploadClusterForm() {
		
	}
	
	
}
