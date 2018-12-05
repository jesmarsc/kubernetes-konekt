package com.kubernetes.konekt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.dao.ClusterDao;
import com.kubernetes.konekt.entity.Cluster;

@Service
public class ClusterServiceImpl implements ClusterService{

	@Autowired
	private ClusterDao clusterDao;
	
	@Override
	@Transactional
	public Cluster getCluster(String clusterUrl) {
		return clusterDao.getCluster(clusterUrl);
	}
	
	@Override
	@Transactional
	public List<Cluster> getAllClusters() {
		return clusterDao.getAllClusters();
	}

	@Override
	@Transactional
	public boolean saveCluster(Cluster newCluster) {
		return clusterDao.saveCluster(newCluster);
	}

	@Override
	@Transactional
	public void deleteCluster(Cluster cluster) {
		clusterDao.deleteCluster(cluster);
	}

	@Override
	@Transactional
	public List<Cluster> getAllAvailableClusters() {
		return clusterDao.getAllAvailableClusters();
	}

	@Override
	@Transactional
	public void updateEntry(Cluster updateCluster) {
		clusterDao.updateEntry(updateCluster);		
	}

}
