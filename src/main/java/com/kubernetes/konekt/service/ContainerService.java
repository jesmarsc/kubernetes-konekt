package com.kubernetes.konekt.service;

import com.kubernetes.konekt.entity.Container;

public interface ContainerService {
	public boolean saveContainer(Container newContainer);

	public Container getContainerByContainerPath(String containerPath);

	public void deleteContainer(Container containerTBD);

	public boolean containerExists(String containerPath);
	
	

}
