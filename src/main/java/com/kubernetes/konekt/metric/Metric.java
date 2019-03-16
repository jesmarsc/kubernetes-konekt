package com.kubernetes.konekt.metric;

import com.kubernetes.konekt.entity.Cluster;

public class Metric implements Comparable<Metric>{
    private Cluster cluster;
    private double cpu;
    private double mem;
    private double net;
    private double loss;

    public Metric(Cluster cluster, double cpu, double mem, double net, double loss) {
        this.cluster = cluster;
        this.cpu = cpu;
        this.mem = mem;
        this.net = net;
        this.loss = loss;
    }
    public Cluster getCluster() {
        return cluster;
    }
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    public double getCpu() {
        return cpu;
    }
    public void setCpu(double cpu) {
        this.cpu = cpu;
    }
    public double getMem() {
        return mem;
    }
    public void setMem(double mem) {
        this.mem = mem;
    }
    public double getNet() {
        return net;
    }
    public void setNet(double net) {
        this.net = net;
    }
    public double getLoss() { return loss; }
    public void setLoss(double loss) { this.loss = loss; }
    @Override
    public String toString() {
        return "Metric [cpu=" + cpu + ", mem=" + mem + ", net=" + net + "]";
    }
    @Override
    public int compareTo(Metric arg0) {
        double normalizeLeftNet = this.getNet()/(this.getNet()+arg0.getNet());
        double normalizeRightNet = arg0.getNet()/(arg0.getNet()+this.getNet());
        double left = this.getCpu() + this.getMem() + normalizeLeftNet;
        double right = arg0.getCpu() + arg0.getMem() + normalizeRightNet;
        return Double.compare(left, right);
    }
}
