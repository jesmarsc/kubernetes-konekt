package com.kubernetes.konekt.form;

import javax.validation.constraints.NotNull;

public class UploadContainerToClusterForm {
	
	@NotNull(message = "Required Field")
	private String yaml;

	private String clusterUrl;
	
	
	
	public UploadContainerToClusterForm() {

		this.yaml = new String();
		this.clusterUrl = new String();
	}

	public String getClusterUrl() {
		return clusterUrl;
	}

	public void setClusterUrl(String clusterUrl) {
		this.clusterUrl = clusterUrl;
	}

	public String getYaml() {
		return yaml;
	}

	public void setYaml(String yaml) {
		this.yaml = yaml;
	}


	
}
