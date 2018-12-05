package com.kubernetes.konekt.dao;

import com.kubernetes.konekt.entity.Container;

public interface ContainerDao {
	
	public Container getContainerByName(String name);
	
	public boolean saveContainer(Container newContainer);

	public void deleteContainer(Container containerTBD);

	public boolean containerExists(String name);

	public void updateEntry(Container updateContainer);
	

}
