package com.kubernetes.konekt.service;

import java.util.List;

import com.kubernetes.konekt.entity.Container;

public interface ContainerService {
	
	public Container getContainerById(Long id);
	
	public Container getContainerByName(String name);
	
	public boolean saveContainer(Container newContainer);

	public void deleteContainer(Container containerTBD);

	public boolean containerExists(String name);

	public void updateEntry(Container updateContainer);

	public List<Container> getContainerByClusterUrl(String clusterUrl);
	
}
