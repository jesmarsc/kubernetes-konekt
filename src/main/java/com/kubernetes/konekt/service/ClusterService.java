package com.kubernetes.konekt.service;

import java.util.List;

import com.kubernetes.konekt.entity.Cluster;

public interface ClusterService  {
	
	public Cluster getCluster(String clusterUrl);
	
	public List<Cluster> getAllClusters();
	
	public void deleteCluster(Cluster cluster);

	public void updateEntry(Cluster updateCluster);

	public Cluster getClusterByPrometheusServiceUid(String uid);

}
