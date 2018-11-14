package com.kubernetes.konekt.dao;

import java.util.List;

import javax.persistence.EntityManager;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kubernetes.konekt.entity.Cluster;

@Repository
public class ClusterDAOImpl implements ClusterDAO {
	
	@Autowired
	private EntityManager factory;

	@Override
	public List<Cluster> getAllClusters() {
		Session currentSession = factory.unwrap(Session.class);

		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster", Cluster.class);
		
		List<Cluster> clusters = theQuery.getResultList();
		
		return clusters;
	}

	@Override
	public boolean doesClusterExist(String ClusterIp) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster WHERE ip = :ip ", Cluster.class);
		theQuery.setParameter("ip",ClusterIp);
		List<Cluster> clusters = theQuery.getResultList();
		
		if(!clusters.isEmpty()) {
			return true;
		}
	
		return false;
	}

	@Override
	public boolean saveCluster(Cluster newCluster) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster where ip = :ip ", Cluster.class);
		
		theQuery.setParameter("ip", newCluster.getId());
		
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
		Transaction currentTransaction = currentSession.getTransaction();
		currentTransaction.begin();
		
		
		// cannot specify query type because query created is native thats why warning is being surpressed
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createNativeQuery("DELETE FROM cluster_info WHERE id = :id ");
		query.setParameter("id",cluster.getId());
		
		int result = query.executeUpdate();
		System.out.println("\n\n\n\n\n" + result + "\n\n\n\n\n");
		currentTransaction.commit();
		
		currentSession.close();
	}

	@Override
	public Cluster getCluster(String ClusterIp) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Cluster> theQuery = 
				currentSession.createQuery("FROM Cluster WHERE ip = :ip ", Cluster.class);
		theQuery.setParameter("ip",ClusterIp);
		Cluster cluster = theQuery.getSingleResult();
		
		return cluster;
		

	}

	

}
