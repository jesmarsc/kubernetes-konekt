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
	public void deleteCluster(Cluster cluster) {
		Session currentSession = factory.unwrap(Session.class);
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createNativeQuery("DELETE FROM cluster_info WHERE id = :id ");
		query.setParameter("id", cluster.getId());
		query.executeUpdate();
	}


	@Override
	public void updateEntry(Cluster updateCluster) {
		Session currentSession = factory.unwrap(Session.class);
		currentSession.saveOrUpdate(updateCluster) ;
	}

	@Override
	public Cluster getClusterByPrometheusServiceUid(String uid) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster WHERE prometheus_uid = :uid ", Cluster.class);
		theQuery.setParameter("uid", uid);
		Cluster cluster = null;
		
		try {
			cluster = theQuery.getSingleResult();
		} catch (Exception e) {
			cluster = null;
		}
		
		return cluster;
	}
  
}
