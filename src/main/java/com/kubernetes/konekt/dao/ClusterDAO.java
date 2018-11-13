package com.kubernetes.konekt.dao;

import java.util.List;

import com.kubernetes.konekt.entity.Cluster;

public interface ClusterDAO {
	
	public List<Cluster> getClusters(int providerId);
	public List<Cluster> getAllClusters();


}
