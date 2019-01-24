package com.kubernetes.konekt.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kubernetes.konekt.entity.Cluster;

@Repository
public class ClusterDaoImpl implements ClusterDao {
	
	@Autowired
	private EntityManager factory;
	
	@Override
	public Cluster getCluster(String clusterUrl) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster WHERE cluster_url = :cluster_url ", Cluster.class);
		theQuery.setParameter("cluster_url", clusterUrl);
		Cluster cluster = null;
		
		try {
			cluster = theQuery.getSingleResult();
		} catch (Exception e) {
			cluster = null;
		}
		
		return cluster;
	}

	@Override
	public List<Cluster> getAllClusters() {
		Session currentSession = factory.unwrap(Session.class);

		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster", Cluster.class);
		List<Cluster> clusters = theQuery.getResultList();
		
		return clusters;
	}

	@Override
	public boolean saveCluster(Cluster newCluster) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster where id = :id ", Cluster.class);
		
		theQuery.setParameter("id", newCluster.getId());
		
		List<Cluster> matchingClusterIps = theQuery.getResultList();
		
		if(!matchingClusterIps.isEmpty()) {
			return false;
		}
		currentSession.save(newCluster);
		return true;
	}

	@Override
	public void deleteCluster(Cluster cluster) {
		Session currentSession = factory.unwrap(Session.class);
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createNativeQuery("DELETE FROM cluster_info WHERE id = :id ");
		query.setParameter("id", cluster.getId());
		query.executeUpdate();
	}
  
	@Override
	public List<Cluster> getAllAvailableClusters() {
		Session currentSession = factory.unwrap(Session.class);
		String containerName = "N/A";
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster WHERE containerName = :containerName ", Cluster.class);
		theQuery.setParameter("containerName",containerName);
		List<Cluster> clusters = theQuery.getResultList();
		return clusters;
	}

	@Override
	public void updateEntry(Cluster updateCluster) {
		Session currentSession = factory.unwrap(Session.class);
		currentSession.saveOrUpdate(updateCluster) ;
	}
  
}
