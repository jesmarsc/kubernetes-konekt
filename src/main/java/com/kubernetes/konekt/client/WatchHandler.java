package com.kubernetes.konekt.client;

import com.kubernetes.konekt.dao.ClusterDaoImpl;
import com.kubernetes.konekt.entity.Cluster;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;


public class WatchHandler implements Runnable {
	
	
	private ClusterDaoImpl clusterDao;
	
	private ClusterApi clusterApi;

	private String url;
	private String user;
	private String pass;

	private Boolean shutDown = false;
	
	public WatchHandler(String url, String user, String pass ) {
		this.url = url;
		this.user = user;
		this.pass = pass;
		clusterApi = new ClusterApi();
		clusterApi.setupClient(url, user, pass);
		clusterDao = new ClusterDaoImpl();
	}
	
	
	
	@Override
	public void run() { 
		while (!shutDown)
        {
            
			
			V1ServiceList result;
			try {
				result = clusterApi.getNamespacedV1ServiceList("monitoring");
			System.out.println("Trying to get prometheus ip");
			for(V1Service item :result.getItems()) {
				if(item.getStatus().getLoadBalancer().getIngress() != null && item.getMetadata().getName().equals("prometheus-k8s")) {
						
					Cluster cluster = clusterDao.getCluster(url);
					System.out.println(item);
					cluster.setPrometheusIp(item.getStatus().getLoadBalancer().getIngress().get(0).getIp());
            		 // set cluster status to running
            		 cluster.setStatus("Ready");
            		 // update cluster 
            		 // TODO: add logic to add cluster to master 
            		 clusterDao.updateEntry(cluster);
            		 // shutdown thread
            		 	shutDown = true;
				}
			}
			} catch (ApiException e) {
				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
        }	
	}
}