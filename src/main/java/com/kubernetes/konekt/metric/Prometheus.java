package com.kubernetes.konekt.metric;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Prometheus {
    
    private static final String masterInstance = "35.247.41.79:9090";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public double getCpuUsage(String instanceUrl) throws IOException {
        String params = "{instance=\""+ instanceUrl + "\",mode=\"idle\"}";
        String query = restTemplate.getForObject(
                "http://" + masterInstance + "/api/v1/query?query="
                + "1-avg(irate(node_cpu_seconds_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getMemUsage(String instanceUrl) throws IOException {
        // {instance="URL"}
        String params = "%7Binstance%3D%22"+ instanceUrl + "%22%7D";
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
    
    public double getNetInputUsage(String instanceUrl) throws IOException {
        String params = "{instance=\""+ instanceUrl + "\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterInstance + "/api/v1/query?query="
                + "sum(irate(node_network_receive_bytes_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getNetOutputUsage(String instanceUrl) throws IOException {
        String params = "{instance=\""+ instanceUrl + "\",device=\"eth0\"}";
        String query = restTemplate.getForObject(
                "http://" + masterInstance + "/api/v1/query?query="
                + "sum(irate(node_network_transmit_bytes_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = objectMapper.readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public double getNetSaturation(String instanceUrl) throws IOException {
        // {instance="URL",device="eth0"}
        String params = "%7Binstance%3D%22"+ instanceUrl + "%22,device%3D%22eth0%22%7D";
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

}
