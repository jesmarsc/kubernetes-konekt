package com.kubernetes.konekt.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kubernetes.konekt.entity.Container;

@Repository
public class ContainerDAOImpl implements ContainerDAO {

	@Autowired
	private EntityManager factory;
	
	@Override
	public boolean saveContainer(Container newContainer) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Container> theQuery = 
				currentSession.createQuery("FROM Container where containerPath = :containerPath ", Container.class);
		
		theQuery.setParameter("containerPath", newContainer.getContainerPath());
		
		List<Container> matchingContainerPaths = theQuery.getResultList();
		
		if(!matchingContainerPaths.isEmpty()) {
			System.out.println("\n\n\n\n\n\n" + matchingContainerPaths + "\n\n\n\n\n");
			return false;
		}
		
		currentSession.save(newContainer);
		return true;
		
	}

	
	

}
