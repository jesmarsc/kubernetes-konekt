package com.kubernetes.konekt.dao;

import java.util.List;

import com.kubernetes.konekt.entity.Cluster;

public interface ClusterDAO {
	

	public List<Cluster> getAllClusters();
	public boolean doesClusterExist(String ClusterIp);
	public boolean saveCluster(Cluster newCluster);


}
