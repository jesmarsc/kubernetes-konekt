package com.kubernetes.konekt.dao;

import java.util.List;

import com.kubernetes.konekt.entity.Container;

public interface ContainerDao {
	
	public Container getContainerByName(String name);

	public void deleteContainer(Container containerTBD);

	public boolean containerExists(String name);

	public void updateEntry(Container updateContainer);

	public Container getContainerById(Long id);

	public List<Container> getContainerByClusterUrl(String clusterUrl);

	public List<Container> getContainersByProviderId(Long id);
	

}
