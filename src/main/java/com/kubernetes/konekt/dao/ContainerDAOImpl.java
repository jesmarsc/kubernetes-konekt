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
			return false;
		}
		
		currentSession.save(newContainer);
		return true;
		
	}

	@Override
	public Container getContainerByContainerPath(String containerPath) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Container> theQuery = 
				currentSession.createQuery("FROM Container WHERE containerPath = :containerPath ", Container.class);
		
		theQuery.setParameter("containerPath", containerPath);
		
		Container container = theQuery.getSingleResult();
		
		return container;
	}

	@Override
	public void deleteContainer(Container containerTBD) {
		Session currentSession = factory.unwrap(Session.class);
		
		// cannot specify query type because query created is native thats why warning is being surpressed
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createNativeQuery("DELETE FROM container_info WHERE id = :id ");
		query.setParameter("id",containerTBD.getId());
		query.executeUpdate();
		
	}

	@Override
	public boolean containerExists(String containerPath) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Container> theQuery = 
				currentSession.createQuery("FROM Container WHERE containerPath = :containerPath ", Container.class);
		
		theQuery.setParameter("containerPath", containerPath);
		
		List<Container> matchingContainerPaths = theQuery.getResultList();
		
		if(!matchingContainerPaths.isEmpty()) {
			return true;
		}
		else {
			return false;
		}		
	}

	
	

}
