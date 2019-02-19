package com.kubernetes.konekt;

import java.io.IOException;

import com.kubernetes.konekt.client.ClusterApi;
import com.kubernetes.konekt.client.WatchHandler;
import com.kubernetes.konekt.entity.Cluster;

import io.kubernetes.client.ApiException;

public class TestCrdCreate {
	
	
	public static void main(String[] args) throws ApiException, IOException {
		ClusterApi test = new ClusterApi();
		Cluster clust = new Cluster();
		test.setupClient("https://104.196.125.196", "admin", "DTNjSbyqgfO960Ht");
		Thread thread = new Thread(new WatchHandler("https://104.196.125.196", "admin", "DTNjSbyqgfO960Ht"));
    	thread.start();
		//test.setupPrometheus(new Long(2),new Cluster());
		//test.crdCreate();

	}

}
