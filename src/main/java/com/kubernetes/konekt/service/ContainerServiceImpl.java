package com.kubernetes.konekt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.dao.ContainerDao;
import com.kubernetes.konekt.entity.Container;

@Service
public class ContainerServiceImpl implements ContainerService{
	
	@Autowired 
	private ContainerDao containerDao;
	
	@Override
	@Transactional
	public Container getContainerByName(String name) {
		return containerDao.getContainerByName(name);
	}
	
	@Override
	@Transactional
	public boolean saveContainer(Container newContainer) {
		return containerDao.saveContainer(newContainer);
	}

	@Override
	@Transactional
	public void deleteContainer(Container containerTBD) {
		containerDao.deleteContainer(containerTBD);
		
	}

	@Override
	@Transactional
	public boolean containerExists(String name) {
		return containerDao.containerExists(name);
		
	}

	@Override
	@Transactional
	public void updateEntry(Container updateContainer) {
		containerDao.updateEntry(updateContainer);
		
	}

}
