package com.kubernetes.konekt;

import com.kubernetes.konekt.client.ClusterApi;

public class TestNamespaceExist {
	public static void main(String[] args) {
		String url = "https://35.230.93.241";
		String username = "admin";
		String password = "n4GCqML8IYhPjePc";
		String namespace = "bob";
		ClusterApi clusterApi = new ClusterApi();
		
		Boolean doesExist = clusterApi.checkNamespaceAlreadyExist(namespace, url, username, password);
		if(doesExist) {
			System.out.println("\n\n Namespace exist \n\n");
		}
	}

}
