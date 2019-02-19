package com.kubernetes.konekt.metric;

public class Metric {
    private double cpu;
    private double mem;
    private double net;
    
    public Metric() {
        cpu = 0.0;
        mem = 0.0;
        net = 0.0;
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
}
