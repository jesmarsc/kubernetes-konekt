package com.kubernetes.konekt.dao;

import java.util.List;

import com.kubernetes.konekt.entity.Cluster;

public interface ClusterDao {

	public Cluster getCluster(String clusterUrl);
	
	public List<Cluster> getAllClusters();
	
	public boolean saveCluster(Cluster newCluster);
	
	public void deleteCluster(Cluster cluster);

	public List<Cluster> getAllAvailableClusters();

	public void updateEntry(Cluster updateCluster);

}
