package com.kubernetes.konekt;

import java.io.IOException;

import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;

public class TestCrdCreate {
	
	
	public static void main(String[] args) throws ApiException, IOException {
		ClusterApi test = new ClusterApi();
		test.crdCreate("https://35.247.107.39", "admin", "HvH8Xwgrcf7toHvQ");

	}

}
