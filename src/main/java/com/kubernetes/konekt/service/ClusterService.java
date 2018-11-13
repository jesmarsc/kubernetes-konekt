package com.kubernetes.konekt.service;

import java.util.List;

import com.kubernetes.konekt.entity.Cluster;

public interface ClusterService  {
	
	
	public List<Cluster> getClusters(int providerId);
	public List<Cluster> getAllClusters();


}
