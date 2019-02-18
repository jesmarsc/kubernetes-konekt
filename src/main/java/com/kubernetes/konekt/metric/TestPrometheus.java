package com.kubernetes.konekt.metric;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.utils.IOUtils;

import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Secret;
import io.kubernetes.client.util.Yaml;

public class TestPrometheus {
    
    private ClusterApi clusterApi;

    public void addPrometheusInstance(String instanceIp) throws IOException {
        
        FileReader fileReader = new FileReader("prometheus-federation.yaml");
        ArrayList<Object> topArray = Yaml.loadAs(fileReader, ArrayList.class);
        Map<String, Object> topMap = (HashMap) topArray.get(0);
        ArrayList<Object> static_configs = (ArrayList) topMap.get("static_configs");
        Map<String, Object> targets = (HashMap) static_configs.get(0);
        ArrayList<String> instances = (ArrayList) targets.get("targets");
        instances.add(instanceIp);
        fileReader.close();
        
        FileWriter fileWriter = new FileWriter("prometheus-federation.yaml");
        Yaml.dump(topArray, fileWriter);
        fileWriter.close();
    }
    
    public void removePrometheusInstance(String instanceIp) throws IOException {
        
        FileReader fileReader = new FileReader("prometheus-federation.yaml");
        ArrayList<Object> topArray = Yaml.loadAs(fileReader, ArrayList.class);
        Map<String, Object> topMap = (HashMap) topArray.get(0);
        ArrayList<Object> static_configs = (ArrayList) topMap.get("static_configs");
        Map<String, Object> targets = (HashMap) static_configs.get(0);
        ArrayList<String> instances = (ArrayList) targets.get("targets");
        instances.remove(instanceIp);
        fileReader.close();
        
        FileWriter fileWriter = new FileWriter("prometheus-federation.yaml");
        Yaml.dump(topArray, fileWriter);
        fileWriter.close();
    }
    
    public void updatePrometheusSecret() throws IOException, ApiException {
        clusterApi = new ClusterApi();
        byte[] bytes = IOUtils.toByteArray(new FileInputStream("prometheus-federation.yaml"));
        Map<String, byte[]> data = new HashMap<String, byte[]>();
        data.put("prometheus-federation.yaml", bytes);
        V1Secret body = new V1Secret();
        body.setData(data);
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName("prometheus-additional-configs");
        meta.setNamespace("monitoring");
        body.setMetadata(meta);
        
        //clusterApi.createSecret(meta.getNamespace(), body);
        clusterApi.replaceSecret(meta.getName(), meta.getNamespace(), body);
    }
    
    public static void main(String[] args) throws IOException, ApiException {
        TestPrometheus temp = new TestPrometheus();
        
        temp.removePrometheusInstance("35.247.41.79:9090");
        temp.updatePrometheusSecret();
    }

}
