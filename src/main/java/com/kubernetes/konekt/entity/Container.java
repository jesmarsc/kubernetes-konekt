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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "container_info")
@JsonIgnoreProperties({"account"})
public class Container {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "container_name")
	private String containerName;

	@Column(name = "cluster_url")
	private String clusterUrl;

	@Column(name = "kind")
	private String kind;

	@Column(name = "container_status")
	private String status;
	
	@Column(name = "container_uid")
	private String uid;

	@Column(name = "provider_id")
	private Long providerId;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "user_account_id")
	private Account account;

	public Container() {

	}

	public Container(String containerName, String kind, String status, String clusterUrl, Long providerId, String uid) {

		this.containerName = containerName;
		this.clusterUrl = clusterUrl;
		this.kind = kind;
		this.status = status;
		this.kind = kind;
		this.providerId = providerId;
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
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
