package com.kubernetes.konekt.client;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.service.ClusterService;

import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Watch;


public class WatchHandler implements Runnable {
	
	@Autowired
	private ClusterService clusterService;
	
	private Watch<V1Service> watch;
	private Boolean shutDown = false;
	
	public WatchHandler(Watch<V1Service> watch) {
		this.watch = watch;
	}
	@Override
	public void run() {
		while (!shutDown)
        {
            System.out.println(new Date()+"WatchHandler Runnable");
            try
            {
            	// Change database cluster status from pending to running 
            	 for (Watch.Response<V1Service> item : watch) {
                	 if(item.object.getStatus().getLoadBalancer().getIngress() != null && item.object.getMetadata().getName().equals("prometheus-k8s")) {
                		 // get cluster using uid
                		 Cluster cluster = clusterService.getClusterByPrometheusServiceUid(item.object.getMetadata().getUid());
                		 // add prometheus load balancer ip to db
                		 cluster.setPrometheusIp(item.object.getStatus().getLoadBalancer().getIngress().get(0).getIp());
                		 // set cluster status to running
                		 cluster.setStatus("Ready");
                		 // update cluster 
                		 // TODO: add logic to add cluster to master 
                		 clusterService.updateEntry(cluster);
                		 // shutdown thread
                		 shutDown = true;
                	 }
                		
                	 
                 }
                 
            }
            catch (Throwable e)
            {
                System.out.println(e);
                try
                {
                    Thread.sleep(1000*5);
                }
                catch (InterruptedException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
		
	}

}
