package com.kubernetes.konekt.metric;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kubernetes.konekt.client.ClusterApi;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Secret;
import io.kubernetes.client.util.Yaml;

@Component
public class Prometheus {
    
    private static final String masterInstance = "35.247.41.79:9090";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ClusterApi clusterApi;
    
    public double getCpuUsage(String instanceIp) throws IOException {
        String params = "{instance=\""+ instanceIp + "\",mode=\"idle\"}";
        String query = restTemplate.getForObject(
                "http://" + masterInstance + "/api/v1/query?query="
                + "1-avg(irate(node_cpu_seconds_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getMemUsage(String instanceIp) throws IOException {
        // {instance="URL"}
        String params = "%7Binstance%3D%22"+ instanceIp + "%22%7D";
        // Plus signs not properly encoded, must use builder without encoding.
        // Rest template will automatically encode, cannot use.
        // If a URI is provided, Rest Template will not encode automatically.
        // URI builders do no encode plus signs properly either, must manually provide encoded URI.
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://" + masterInstance + "/api/v1/query?query="
                + "1-sum(node_memory_MemFree_bytes" + params
                + "%2Bnode_memory_Cached_bytes" + params
                + "%2Bnode_memory_Buffers_bytes" + params
                + ")/sum(node_memory_MemTotal_bytes" + params + ")")
                .build(true);
        URI uri = uriComponents.toUri();
        String query = restTemplate.getForObject(uri, String.class);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getNetInputUsage(String instanceIp) throws IOException {
        String params = "{instance=\""+ instanceIp + "\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterInstance + "/api/v1/query?query="
                + "sum(irate(node_network_receive_bytes_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getNetOutputUsage(String instanceIp) throws IOException {
        String params = "{instance=\""+ instanceIp + "\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterInstance + "/api/v1/query?query="
                + "sum(irate(node_network_transmit_bytes_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getNetSaturation(String instanceIp) throws IOException {
        // {instance="URL",device="eth0"}
        String params = "%7Binstance%3D%22"+ instanceIp + "%22,device%3D%22eth0%22%7D";
        // Plus signs not properly encoded, must use builder without encoding.
        // Rest template will automatically encode, cannot use.
        // If a URI is provided, Rest Template will not encode automatically.
        // URI builders do no encode plus signs properly either, must manually provide encoded URI.
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://" + masterInstance + "/api/v1/query?query="
                + "sum(irate(node_network_receive_drop_total" + params + "%5B2m%5D))"
                + "%2Bsum(irate(node_network_transmit_drop_total" + params + "%5B2m%5D))")
                .build(true);
        URI uri = uriComponents.toUri();
        String query = restTemplate.getForObject(uri, String.class);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
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
    
    public void replaceAdditionalConfigs() throws IOException, ApiException {
        byte[] bytes = IOUtils.toByteArray(new FileInputStream("prometheus-federation.yaml"));
        Map<String, byte[]> data = new HashMap<String, byte[]>();
        data.put("prometheus-federation.yaml", bytes);
        V1Secret body = new V1Secret();
        body.setData(data);
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName("prometheus-additional-configs");
        meta.setNamespace("monitoring");
        body.setMetadata(meta);
        
        clusterApi.replaceSecret(meta.getName(), meta.getNamespace(), body);
    }

}
