package com.kubernetes.konekt.dao;

import com.kubernetes.konekt.entity.Container;

public interface ContainerDAO {
	
	public boolean saveContainer(Container newContainer);

	public Container getContainerByContainerPath(String containerPath);

	public void deleteContainer(Container containerTBD);
	
	

}
