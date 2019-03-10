package com.kubernetes.konekt;

import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1DeploymentList;
import io.kubernetes.client.models.V1ServiceList;

public class TestGetWorkloadByName {
	
	// add button to user dashboard where they can get more information about a workload they have updloaded aka pull loadbalancer ip address
	public static void main(String[] args) {
		String url = "https://35.238.61.193";
		String username = "admin";
		String password = "OyE0ZQbPbPwZdyLk";
		String namespace = "mary";
		ClusterApi clusterApi = new ClusterApi();
		clusterApi.setupClient(url, username, password);
		try {
			V1DeploymentList dresult = clusterApi.getNamespacedV1DeploymentList(namespace);
			V1ServiceList sresult = clusterApi.getNamespacedV1ServiceList(namespace);
			System.out.println(dresult);
			System.out.println("-----------------------------");
			System.out.println(sresult);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
