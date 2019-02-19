package com.kubernetes.konekt;

import java.util.concurrent.TimeUnit;

import com.google.gson.reflect.TypeToken;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

public class TestSetWatch {

	public static void main(String[] args) {
		//ClusterApi test = new ClusterApi();
		//test.setupClient("https://35.230.9.182", "admin", "r43fPDpBLfjy5bP6");
		ApiClient client;
		client = Config.fromUserPassword("https://35.230.9.182", "admin", "r43fPDpBLfjy5bP6", false);
        client.setDebugging(false);
        client.setBasePath("https://35.230.9.182");
        client.getHttpClient().setReadTimeout(60, TimeUnit.SECONDS);
        Configuration.setDefaultApiClient(client);
        CoreV1Api coreInstance= new CoreV1Api(client);
        
        try {
        	 Watch<V1Service> watch = Watch.createWatch(
                     client,
                     coreInstance.listNamespacedServiceCall("monitoring", "true", null, null, Boolean.FALSE, null, null, null, 60, Boolean.TRUE, null, null),
                     new TypeToken<Watch.Response<V1Service>>(){}.getType());
        	
             for (Watch.Response<V1Service> item : watch) {
            	 
            	 //if(item.object.getStatus().getLoadBalancer().getIngress() != null && item.object.getMetadata().getName().equals("prometheus-k8s")) {
            		 System.out.println(item.object.getMetadata().getUid() + "\n\n\n\n\n\n");
            		// System.out.printf("%s%n", item.object.getStatus().getLoadBalancer().getIngress().get(0).getIp());
            	 //}
             }
             
		
        } catch (ApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		

	}

}
