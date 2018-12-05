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
@Table(name = "cluster_info")
public class Cluster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "cluster_url")
	private String clusterUrl;
	
	@Column(name = "cluster_username")
	private String clusterUsername;
	
	@Column(name = "cluster_password")
	private String clusterPassword;
	
	@Column(name = "cluster_status")
	private String status;

	@ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE,
						 CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="provider_account_id")
	private Account account;
	
	public Cluster() {
	}
	
	public Cluster(String clusterUrl, String clusterUsername, String clusterPassword, String status) {
		this.clusterUrl = clusterUrl;
		this.clusterUsername = clusterUsername;
		this.clusterPassword = clusterPassword;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
		return clusterUrl;
	}

}
