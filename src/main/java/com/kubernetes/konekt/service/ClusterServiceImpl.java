package com.kubernetes.konekt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.dao.ClusterDAO;
import com.kubernetes.konekt.entity.Cluster;

@Service
public class ClusterServiceImpl implements ClusterService{


	@Autowired
	private ClusterDAO clusterDAO;
	

	@Override
	@Transactional
	public List<Cluster> getAllClusters() {
		
		return clusterDAO.getAllClusters();

	}


	@Override
	public boolean doesClusterExist(String ClusterIp) {
		return clusterDAO.doesClusterExist(ClusterIp);
	}


	@Override
	public boolean saveCluster(Cluster newCluster) {
		
		return clusterDAO.saveCluster(newCluster);
	}


	
}
