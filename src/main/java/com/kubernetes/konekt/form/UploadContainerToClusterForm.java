package com.kubernetes.konekt.form;

import javax.validation.constraints.NotNull;

public class UploadContainerToClusterForm {
	
	@NotNull(message = "Required Field")
	private String yaml;

	
	public UploadContainerToClusterForm() {
		
	}

	public String getYaml() {
		return yaml;
	}

	public void setYaml(String yaml) {
		this.yaml = yaml;
	}


	
}
