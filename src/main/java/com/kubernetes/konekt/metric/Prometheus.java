package com.kubernetes.konekt.metric;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
import com.kubernetes.konekt.entity.Cluster;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Secret;
import io.kubernetes.client.util.Yaml;

@Component
public class Prometheus {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private ClusterApi clusterApi;
    
    /*
     * Prometheus LoadBalancer IP
     */
    private static final String masterPrometheus = "35.199.188.36:9090";
    
    /*
     * Point to master cluster
     */
    @Autowired
    public Prometheus(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        clusterApi = new ClusterApi();
        clusterApi.setupClient("https://35.185.210.221", "admin", "gQUd1wSvMAssWQtD");
    }
    
    public Metric getUsageMetric(Cluster cluster) {
        String instanceIp = cluster.getClusterUrl().substring(8);
        double cpuPercent = getCpuUsage(instanceIp);
        double memPercent = getMemUsage(instanceIp);
        double bytesPerSecond = getNetInputUsage(instanceIp) + getNetOutputUsage(instanceIp);
        double packetLoss = getNetSaturation(instanceIp);
        return new Metric(cluster, cpuPercent, memPercent, bytesPerSecond, packetLoss);
    }

    private double parseQuery(String query) {
        JsonNode node = null;
        try {
            node = objectMapper.readTree(query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(node.get("data").get("result").get(0) == null) {
            return 1;
        }
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    private double getCpuUsage(String instanceIp) {
        String params = "{instance=\""+ instanceIp + ":443\",mode=\"idle\"}";
        String query = restTemplate.getForObject(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "1-avg(irate(node_cpu_seconds_total{instance}[2m]))", 
                String.class, params);
        return parseQuery(query);
    }
    
    /*
     * Plus signs are not properly encoded through the RestTemplate.
     * If a URI is provided, RestTemplate will not encode automatically.
     * URI builders do not encode plus signs properly either, must manually create encoded URI.
     */
    private double getMemUsage(String instanceIp) {
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
        return parseQuery(query);
    }
    
    private double getNetInputUsage(String instanceIp) {
        String params = "{instance=\""+ instanceIp + ":443\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "sum(irate(node_network_receive_bytes_total{instance}[2m]))", 
                String.class, params);
        return parseQuery(query);
    }
    
    private double getNetOutputUsage(String instanceIp) {
        String params = "{instance=\""+ instanceIp + ":443\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "sum(irate(node_network_transmit_bytes_total{instance}[2m]))", 
                String.class, params);
        return parseQuery(query);
    }
    
    /*
     * Plus signs are not properly encoded through the RestTemplate.
     * If a URI is provided, RestTemplate will not encode automatically.
     * URI builders do not encode plus signs properly either, must manually create encoded URI.
     */
    public double getNetSaturation(String instanceIp) {
        // {instance="URL",device="eth0"}
        String params = "%7Binstance%3D%22"+ instanceIp + ":443%22,device%3D%22eth0%22%7D";
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://" + masterPrometheus + "/api/v1/query?query="
                + "sum(irate(node_network_receive_drop_total" + params + "%5B2m%5D))"
                + "%2Bsum(irate(node_network_transmit_drop_total" + params + "%5B2m%5D))")
                .build(true);
        URI uri = uriComponents.toUri();
        String query = restTemplate.getForObject(uri, String.class);
        return parseQuery(query);
    }
    
    @SuppressWarnings("unchecked")
    public void addCluster(String instanceIp, String username, String password) throws IOException, ApiException {
        
        FileReader templateReader = new FileReader("prometheus-template.yaml");
        ArrayList<Map<String, Object>> yaml = Yaml.loadAs(templateReader, ArrayList.class);
        templateReader.close();
        
        Map<String, Object> map = yaml.get(0);
        map.put("job_name", instanceIp);
        
        ArrayList<Object> static_configs = (ArrayList<Object>) map.get("static_configs");
        Map<String, Object> targets = (HashMap<String, Object>) static_configs.get(0);
        ArrayList<String> instances = (ArrayList<String>) targets.get("targets");
        instances.add(instanceIp);
        
        Map<String, Object> basic_auth = (HashMap<String, Object>) map.get("basic_auth");
        basic_auth.put("username", username);
        basic_auth.put("password", password);
        
        V1Secret secret = clusterApi.getSecret("prometheus-additional-configs", "monitoring");
        Map<String, byte[]> data = secret.getData();
        String config = new String(data.get("prometheus-federation.yaml"), StandardCharsets.UTF_8);
        ArrayList<Map<String, Object>> configYaml = Yaml.loadAs(config, ArrayList.class);
        configYaml.add(map);
        data.put("prometheus-federation.yaml", Yaml.dump(configYaml).getBytes());

        clusterApi.replaceSecret(secret.getMetadata().getName(), secret.getMetadata().getNamespace(), secret);
    }

    @SuppressWarnings("unchecked")
    public void removeCluster(String instanceIp) throws ApiException {
        
        V1Secret secret = clusterApi.getSecret("prometheus-additional-configs", "monitoring");
        Map<String, byte[]> data = secret.getData();
        String config = new String(data.get("prometheus-federation.yaml"), StandardCharsets.UTF_8);
        ArrayList<Map<String, Object>> configYaml = Yaml.loadAs(config, ArrayList.class);
        
        Iterator<Map<String, Object>> iterator = configYaml.iterator();
        while(iterator.hasNext()) {
            Map<String, Object> job = iterator.next();
            if(job.get("job_name").equals(instanceIp)){
                iterator.remove();
                break;
            }
        }
        data.put("prometheus-federation.yaml", Yaml.dump(configYaml).getBytes());
        clusterApi.replaceSecret(secret.getMetadata().getName(), secret.getMetadata().getNamespace(), secret);
    }
    
    public void createAdditionalConfigsSecret() throws IOException, ApiException {
        byte[] bytes = IOUtils.toByteArray(new FileInputStream("prometheus-federation.yaml"));
        Map<String, byte[]> data = new HashMap<>();
        data.put("prometheus-federation.yaml", bytes);
        V1Secret body = new V1Secret();
        body.setData(data);
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName("prometheus-additional-configs");
        meta.setNamespace("monitoring");
        body.setMetadata(meta);
        
        clusterApi.createSecret(meta.getNamespace(), body);
    }
}
