package com.kubernetes.konekt.metric;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private ClusterApi clusterApi;
    
    private static final String masterPrometheus = "35.247.41.79:9090";
    
    public Prometheus() {
        clusterApi = new ClusterApi("https://35.247.84.239", "admin", "5hsiDOChHW9GW5Pw");
    }
    
    public Metric getUsageMetric(String instanceIp) throws IOException {
        Metric metric = new Metric();
        double cpuPercent = getCpuUsage(instanceIp) * 100;
        double memPercent = getMemUsage(instanceIp) * 100;
        double kiloBytesPerSecond = (getNetInputUsage(instanceIp)+getNetOutputUsage(instanceIp))/1000;
        metric.setCpu(cpuPercent);
        metric.setMem(memPercent);
        metric.setNet(kiloBytesPerSecond);
        return metric;
    }
    
    public Map<String, Double> getUsageMap(String instanceIp) throws IOException {
        Map<String, Double> map = new HashMap<String, Double>();
        double cpuPercent = getCpuUsage(instanceIp) * 100;
        double memPercent = getMemUsage(instanceIp) * 100;
        double kiloBytesPerSecond = (getNetInputUsage(instanceIp)+getNetOutputUsage(instanceIp))/1000;
        map.put("cpu", cpuPercent);
        map.put("mem", memPercent);
        map.put("net", kiloBytesPerSecond);
        return map;
    }
    
    public double getCpuUsage(String instanceIp) throws IOException {
        String params = "{instance=\""+ instanceIp + ":443\",mode=\"idle\"}";
        String query = restTemplate.getForObject(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "1-avg(irate(node_cpu_seconds_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        if(node.get("data").get("result").get(0) == null) {
            return 0;
        }
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    /*
     * Plus signs are not properly encoded through the RestTemplate.
     * If a URI is provided, RestTemplate will not encode automatically.
     * URI builders do not encode plus signs properly either, must manually create encoded URI.
     */
    public double getMemUsage(String instanceIp) throws IOException {
        // {instance="URL"}
        String params = "%7Binstance%3D%22"+ instanceIp + ":443%22%7D";
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "1-sum(node_memory_MemFree_bytes" + params
                + "%2Bnode_memory_Cached_bytes" + params
                + "%2Bnode_memory_Buffers_bytes" + params
                + ")/sum(node_memory_MemTotal_bytes" + params + ")")
                .build(true);
        URI uri = uriComponents.toUri();
        String query = restTemplate.getForObject(uri, String.class);
        JsonNode node = objectMapper.readTree(query);
        if(node.get("data").get("result").get(0) == null) {
            return 0;
        }
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getNetInputUsage(String instanceIp) throws IOException {
        String params = "{instance=\""+ instanceIp + ":443\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "sum(irate(node_network_receive_bytes_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        if(node.get("data").get("result").get(0) == null) {
            return 0;
        }
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getNetOutputUsage(String instanceIp) throws IOException {
        String params = "{instance=\""+ instanceIp + ":443\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "sum(irate(node_network_transmit_bytes_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        if(node.get("data").get("result").get(0) == null) {
            return 0;
        }
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    /*
     * Plus signs are not properly encoded through the RestTemplate.
     * If a URI is provided, RestTemplate will not encode automatically.
     * URI builders do not encode plus signs properly either, must manually create encoded URI.
     */
    public double getNetSaturation(String instanceIp) throws IOException {
        // {instance="URL",device="eth0"}
        String params = "%7Binstance%3D%22"+ instanceIp + ":443%22,device%3D%22eth0%22%7D";
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "sum(irate(node_network_receive_drop_total" + params + "%5B2m%5D))"
                + "%2Bsum(irate(node_network_transmit_drop_total" + params + "%5B2m%5D))")
                .build(true);
        URI uri = uriComponents.toUri();
        String query = restTemplate.getForObject(uri, String.class);
        JsonNode node = objectMapper.readTree(query);
        if(node.get("data").get("result").get(0) == null) {
            return 0;
        }
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public void addCluster(String instanceIp, String username, String password) throws IOException, ApiException {
        
        FileReader templateReader = new FileReader("prometheus-template.yaml");
        
        ArrayList<Object> yaml = Yaml.loadAs(templateReader, ArrayList.class);
        Map<String, Object> map = (HashMap) yaml.get(0);
        map.put("job_name", instanceIp);
        
        ArrayList<Object> static_configs = (ArrayList) map.get("static_configs");
        Map<String, Object> targets = (HashMap) static_configs.get(0);
        ArrayList<String> instances = (ArrayList) targets.get("targets");
        instances.add(instanceIp);
        
        Map<String, Object> basic_auth = (HashMap) map.get("basic_auth");
        basic_auth.put("username", username);
        basic_auth.put("password", password);
        templateReader.close();
        
        FileReader configReader = new FileReader("prometheus-federation.yaml");
        ArrayList<Object> configYaml = Yaml.loadAs(configReader, ArrayList.class);
        configYaml.add(map);
        configReader.close();
        
        FileWriter fileWriter = new FileWriter("prometheus-federation.yaml");
        Yaml.dump(configYaml, fileWriter);
        fileWriter.close();
        this.updateAdditionalConfigs();
    }
    
    public void removeCluster(String instanceIp) throws IOException, ApiException {
        
        FileReader configReader = new FileReader("prometheus-federation.yaml");
        ArrayList<Map<String, Object>> configYaml = Yaml.loadAs(configReader, ArrayList.class);
        Iterator<Map<String, Object>> iterator = configYaml.iterator();
        
        while(iterator.hasNext()) {
            Map<String, Object> job = iterator.next();
            if(job.get("job_name").equals(instanceIp)){
                iterator.remove();
                break;
            }
        }
        
        FileWriter fileWriter = new FileWriter("prometheus-federation.yaml");
        Yaml.dump(configYaml, fileWriter);
        fileWriter.close();
        this.updateAdditionalConfigs();
    }
    
    public void addPrometheusInstance(String instanceIp) throws IOException, ApiException {
        
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
        
        this.updateAdditionalConfigs();
    }
    
    public void removePrometheusInstance(String instanceIp) throws IOException, ApiException {
        
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
        
        this.updateAdditionalConfigs();
    }
    
    public void updateAdditionalConfigs() throws IOException, ApiException {
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
    
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static void main(String[] args) throws IOException, ApiException {
        Prometheus test = new Prometheus();
        test.setObjectMapper(new ObjectMapper());
        test.setRestTemplate(new RestTemplate());
        /*
        test.removeCluster("what");
        test.addCluster("35.247.84.239", "admin", "5hsiDOChHW9GW5Pw");
        test.updateAdditionalConfigs();
        */
    }

}
