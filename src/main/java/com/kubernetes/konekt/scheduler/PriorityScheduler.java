package com.kubernetes.konekt.scheduler;

import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.metric.Metric;
import com.kubernetes.konekt.metric.Prometheus;
import com.kubernetes.konekt.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class PriorityScheduler {
    private PriorityBlockingQueue<Metric> priorityQueue;
    private final ClusterService clusterService;
    private final Prometheus prometheus;

    @Autowired
    public PriorityScheduler(ClusterService clusterService, Prometheus prometheus) {
        this.clusterService = clusterService;
        this.prometheus = prometheus;
        createPriorityQueue();
    }

    public void createPriorityQueue() {
        priorityQueue = new PriorityBlockingQueue<>();
        List<Cluster> clusterList = clusterService.getAllClusters();
        for(Cluster cluster : clusterList) {
            Metric metric = prometheus.getUsageMetric(cluster);
            priorityQueue.add(metric);
        }
    }

    public Cluster getCluster() {
        try {
            Cluster cluster;
            do {
                cluster = priorityQueue.remove().getCluster();
                cluster = clusterService.getCluster(cluster.getClusterUrl());
            } while(cluster == null);
            return cluster;
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addCluster(Cluster cluster) {
        Metric metric = prometheus.getUsageMetric(cluster);
        priorityQueue.add(metric);
    }

    public void drainTo(ArrayList<Metric> arrayList){
        priorityQueue.drainTo(arrayList);
    }
}
