package com.kubernetes.konekt.service;

import java.util.List;

import com.kubernetes.konekt.entity.Cluster;

public interface ClusterService  {
	
	
	public boolean doesClusterExist(String ClusterIp);

	public List<Cluster> getAllClusters();


}
