package com.kubernetes.konekt.form;

import javax.validation.constraints.NotNull;

public class UploadContainerToClusterForm {
	
	@NotNull(message = "Required Field")
	private String containerName;
	
	@NotNull(message = "Required Field")
	private String clusterIp;
	
	public UploadContainerToClusterForm() {}
	
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public String getClusterIp() {
		return clusterIp;
	}
	public void setClusterIp(String clusterIp) {
		this.clusterIp = clusterIp;
	}
	
}
