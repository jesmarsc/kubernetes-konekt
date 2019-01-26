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
	
	@Column(name="cluster_url")
	private String clusterUrl;
	
	@Column(name="kind")
	private String kind;
	
	@Column(name="container_status")
	private String status;
	
	/*
	 Add mapping for each container back to the cluster that it running on to 
	 ease populating provider table that show what is running on their clusters
	 */
	@ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE,
			 			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="user_account_id")
	private Account account;
	

	public Container() {
		
	}
	
	public Container(String containerName, String clusterUrl, String kind, String status) {
		this.containerName = containerName;
		this.clusterUrl = clusterUrl;
		this.kind = kind;
		this.status = status;
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
	
	public String getClusterUrl() {
		return clusterUrl;
	}

	public void setClusterUrl(String clusterUrl) {
		this.clusterUrl = clusterUrl;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	@Override
	public String toString() {
		return containerName;
	}
	
}


