package com.kubernetes.konekt;

import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;

public class TestGetDeploymentsByNamespce {

	public static void main(String[] args) {
		String url = "https://104.198.11.183";
		String username = "admin";
		String password = "bUr485TM8ehSqVeM";
		String namespace = "mary";
		ClusterApi clusterApi = new ClusterApi();
		
		try {
		clusterApi.getDeploymentsByNamespace(namespace);
		}catch(ApiException e) {
			e.printStackTrace();
		}
		

	}

}
