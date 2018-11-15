package com.kubernetes.konekt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.dao.ContainerDAO;
import com.kubernetes.konekt.entity.Container;

@Service
public class ContainerServiceImpl implements ContainerService{
	
	@Autowired 
	ContainerDAO containerDAO;
	
	@Override
	@Transactional
	public boolean saveContainer(Container newContainer) {
	
		return containerDAO.saveContainer(newContainer);
	}

	
}
