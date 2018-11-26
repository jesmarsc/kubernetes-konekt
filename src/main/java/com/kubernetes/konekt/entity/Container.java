package com.kubernetes.konekt.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "container_info")
public class Container {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name="container_name")
	private String containerName;
	
	@Column(name="container_path")
	private String containerPath;
	
	@Column(name="container_status")
	private String status;
	
	@Column(name="container_ip")
	private String ipAddress;
	
	@ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE,
			 			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="user_account_id")
	private Account account;

	public Container() {
		
	}
	
	public Container(String containerName, String containerPath,String status, String ipAddress) {
		super();
		this.status = status;
		this.ipAddress = ipAddress;
		this.containerName = containerName;
		this.containerPath = containerPath;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getContainerPath() {
		return containerPath;
	}

	public void setContainerPath(String containerPath) {
		this.containerPath = containerPath;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString() {
		return containerName;
	}


	
	
}


