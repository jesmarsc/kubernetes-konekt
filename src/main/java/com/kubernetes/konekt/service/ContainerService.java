package com.kubernetes.konekt.service;

import com.kubernetes.konekt.entity.Container;

public interface ContainerService {
	public boolean saveContainer(Container newContainer);

}