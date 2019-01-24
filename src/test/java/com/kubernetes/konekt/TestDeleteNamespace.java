package com.kubernetes.konekt;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.util.Config;
public class TestDeleteNamespace {

	public static void main(String[] args) {
		String url = "https://35.230.93.241";
		String username = "admin";
		String password = "n4GCqML8IYhPjePc";
		String namespace = "bob";
		
		
		
	
		
		
		/*
		ClusterApi clusterApi = new ClusterApi();
		
		Boolean doesExist = clusterApi.CheckNamespaceAlreadyExist(namespace, url, username, password);
		if(doesExist) {
			System.out.println("\n\n Namespace exist \n\n");
		}
		*/

	}

}
