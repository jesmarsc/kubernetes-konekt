package com.kubernetes.konekt.metric;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.kubernetes.client.util.Yaml;

public class TestPrometheus {

    public void addInstance(String instanceIp) throws IOException {
        FileReader fileReader = new FileReader("prometheus-federation.yaml");
        ArrayList<Object> topArray = Yaml.loadAs(fileReader, ArrayList.class);
        Map<String, Object> topMap = (HashMap) topArray.get(0);
        ArrayList<Object> static_configs = (ArrayList) topMap.get("static_configs");
        Map<String, Object> targets = (HashMap) static_configs.get(0);
        ArrayList<String> instances = (ArrayList) targets.get("targets");
        instances.add(instanceIp);
        
        FileWriter fileWriter = new FileWriter("prometheus-federation.yaml");
        Yaml.dump(topArray, fileWriter);
        
    }
    
    public static void main(String[] args) throws IOException {
        TestPrometheus temp = new TestPrometheus();
        temp.addInstance("testing-success");
    }

}
