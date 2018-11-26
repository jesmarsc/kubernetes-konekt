package com.kubernetes.konekt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.dao.ContainerDAO;
import com.kubernetes.konekt.entity.Container;

@Service
public class ContainerServiceImpl implements ContainerService{
	
	@Autowired 
	private ContainerDAO containerDAO;
	
	@Override
	@Transactional
	public boolean saveContainer(Container newContainer) {
	
		return containerDAO.saveContainer(newContainer);
	}

	@Override
	@Transactional
	public Container getContainerByContainerPath(String containerPath) {
		return containerDAO.getContainerByContainerPath(containerPath);
	}

	@Override
	@Transactional
	public void deleteContainer(Container containerTBD) {
		containerDAO.deleteContainer(containerTBD);
		
	}

	@Override
	@Transactional
	public boolean containerExists(String containerPath) {
		return containerDAO.containerExists(containerPath);
		
	}

	@Override
	@Transactional
	public Container getContainerByContainerIp(String clusterIp) {
		return containerDAO. getContainerByContainerIp(clusterIp);
	}

	@Override
	@Transactional
	public void updateEntry(Container updateContainer) {
		containerDAO.updateEntry(updateContainer);
		
	}

	
}
