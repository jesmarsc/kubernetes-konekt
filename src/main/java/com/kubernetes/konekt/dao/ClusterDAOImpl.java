package com.kubernetes.konekt.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kubernetes.konekt.entity.Cluster;

@Repository
public class ClusterDAOImpl implements ClusterDAO {
	
	@Autowired
	private EntityManager factory;
	
	@Override
	public List<Cluster> getClusters(int providerId) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster WHERE ", Cluster.class);
		
		List<Cluster> clusters = theQuery.getResultList();
		
		return clusters;

	}

	@Override
	public List<Cluster> getAllClusters() {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster", Cluster.class);
		
		List<Cluster> clusters = theQuery.getResultList();
		
		return clusters;
	}

}
