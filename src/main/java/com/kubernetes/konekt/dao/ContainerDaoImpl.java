package com.kubernetes.konekt.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kubernetes.konekt.entity.Container;

@Repository
public class ContainerDaoImpl implements ContainerDao {

	@Autowired
	private EntityManager factory;

	@Override
	public Container getContainerByName(String name) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Container> theQuery = 
				currentSession.createQuery("FROM Container WHERE containerName = :containerName ", Container.class);
		
		theQuery.setParameter("containerName", name);
		
		Container container = theQuery.getSingleResult();
		
		return container;
	}
	
	@Override
	public boolean saveContainer(Container newContainer) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Container> theQuery = 
				currentSession.createQuery("FROM Container where containerName = :containerName ", Container.class);
		
		theQuery.setParameter("containerName", newContainer.getContainerName());
		
		List<Container> matchingContainerNames = theQuery.getResultList();
		
		if(!matchingContainerNames.isEmpty()) {
			return false;
		}
		
		currentSession.save(newContainer);
		return true;
		
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
	public boolean containerExists(String name) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Container> theQuery = 
				currentSession.createQuery("FROM Container WHERE containerName = :containerName ", Container.class);
		
		theQuery.setParameter("containerName", name);
		
		List<Container> matchingContainerName = theQuery.getResultList();
		
		if(!matchingContainerName.isEmpty()) {
			return true;
		}
		else {
			return false;
		}		
	}

	@Override
	public void updateEntry(Container updateContainer) {
		Session currentSession = factory.unwrap(Session.class);
		currentSession.saveOrUpdate(updateContainer) ;		
	}

	@Override
	public Container getContainerById(Long id) {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Container> theQuery = 
				currentSession.createQuery("FROM Container WHERE id = :id ", Container.class);
		
		theQuery.setParameter("id", id);
		
		Container container = theQuery.getSingleResult();
		
		return container;
	}

}
