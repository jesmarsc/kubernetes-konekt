package com.kubernetes.konekt;

import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;

public class TestDeleteNamespace {

	public static void main(String[] args) {
		String url = "https://35.230.93.241";
		String username = "admin";
		String password = "n4GCqML8IYhPjePc";
		String namespace = "bob";

		ClusterApi clusterApi = new ClusterApi();
		
		Boolean doesExist;
		try {
			doesExist = clusterApi.checkNamespaceAlreadyExist(namespace, url, username, password);
			if(doesExist) {
				System.out.println("\n\n Namespace exist \n\n");
			}
		} catch (ApiException e) {

			e.printStackTrace();
		}
		


	}

}
