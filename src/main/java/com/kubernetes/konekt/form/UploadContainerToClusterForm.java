package com.kubernetes.konekt.form;

public class UploadContainerToClusterForm {
	private String containerName;
	private String clusterIp;
	
	
	
	public UploadContainerToClusterForm() {

	}
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
