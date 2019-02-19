package com.kubernetes.konekt;

import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;

public class TestNamespaceEmpty {

	public static void main(String[] args) throws ApiException {
		
		String url = "https://35.230.93.241";
		String username = "admin";
		String password = "n4GCqML8IYhPjePc";
		String namespace = "mary";
		ClusterApi clusterApi = new ClusterApi();
		
		Boolean isEmpty = clusterApi.namespaceEmpty(namespace);
		if(isEmpty) {
			System.out.println("\n\n namespace is empty \n\n");
		}
	}

}
