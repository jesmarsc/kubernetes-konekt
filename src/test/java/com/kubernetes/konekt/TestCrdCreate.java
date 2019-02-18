package com.kubernetes.konekt;

import java.io.IOException;

import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;

public class TestCrdCreate {
	
	
	public static void main(String[] args) throws ApiException, IOException {
		ClusterApi test = new ClusterApi();
		test.setupClient("https://35.202.14.162", "admin", "lVOhLxXGygUo0gRt");
		test.setupPrometheus(new Long(2));
		//test.crdCreate();

	}

}
