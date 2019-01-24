package com.kubernetes.konekt;

import com.kubernetes.konekt.client.ClusterApi;

public class TestCreateNamespace {

	public static void main(String[] args) {
		String url = "https://35.230.93.241";
		String username = "admin";
		String password = "n4GCqML8IYhPjePc";
		String namespace = "bob";
		ClusterApi clusterApi = new ClusterApi();
		
		clusterApi.createNamespace(namespace, url, username, password);

	}

}
