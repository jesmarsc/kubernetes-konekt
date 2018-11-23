package com.kubernetes.konekt.form;

import javax.validation.constraints.Pattern;

public class UploadClusterForm {
	
	@Pattern(regexp="^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$",
					message = "Must be valid IPv4 address")
	private String clusterIp;

	
	public UploadClusterForm() {
		
	}

	public String getClusterIp() {
		return clusterIp;
	}

	public void setClusterIp(String clusterIp) {
		this.clusterIp = clusterIp;
	}
}
