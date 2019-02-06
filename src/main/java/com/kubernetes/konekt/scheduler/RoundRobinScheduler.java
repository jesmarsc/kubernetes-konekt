package com.kubernetes.konekt.scheduler;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.service.ClusterService;

@Component
public class RoundRobinScheduler {

	@Autowired 
	private ClusterService clusterService;
	
	public Cluster getNextCluster() {
		
		// get all clusters from database
		List<Cluster> clusterList = clusterService.getAllClusters();
		if(clusterList.isEmpty()) {
			return null; // no clusters on the database
		}
		// find next cluster
		Cluster oldNextCluster = null;
		Cluster newNextCluster = null;
		for(Cluster cluster : clusterList) {
			if(cluster.getRoundRobin() == 1) {
				oldNextCluster = cluster;
				newNextCluster = clusterList.get((clusterList.indexOf(oldNextCluster) + 1) % clusterList.size());
			}
		}
		// if no cluster was selected choose next cluster
		if(oldNextCluster == null) {	
			Random random = new Random();
			Integer next = random.nextInt() % clusterList.size();
			oldNextCluster = clusterList.get(next);
			newNextCluster = clusterList.get((next + 1) % clusterList.size());
		}
		// set next cluster to 0
		oldNextCluster.setRoundRobin(0);
		// set new next cluster value to 1
		newNextCluster.setRoundRobin(1);
		// update database for old next cluster
		clusterService.updateEntry(oldNextCluster);
		// update database for new next cluster
		clusterService.updateEntry(newNextCluster);
		// return cluster
		return oldNextCluster;
	}

}
