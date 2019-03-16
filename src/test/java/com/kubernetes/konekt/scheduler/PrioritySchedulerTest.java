package com.kubernetes.konekt.scheduler;

import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.metric.Metric;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrioritySchedulerTest {

    @Autowired
    PriorityScheduler priorityScheduler;

    @Test
    public void test1CreatePriorityQueue() {
        priorityScheduler.createPriorityQueue();
    }

    @Test
    public void test2AddCluster() {
        for(int i = 0; i < 10; i++) {
            priorityScheduler.addCluster(new Cluster("https://123.123.123.123", null, null, null, null, null));
        }
    }

    @Test
    public void test3GetCluster() {
        priorityScheduler.getCluster();
    }

    public void test4DrainQueue() {
        ArrayList<Metric> metrics = new ArrayList<>();
        priorityScheduler.drainTo(metrics);
        System.out.println(metrics);
    }
}