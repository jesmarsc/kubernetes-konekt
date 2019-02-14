package com.kubernetes.konekt.metric;

import java.io.IOException;
import java.net.URI;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Prometheus {
    
    public double getCpuUsage(String instanceUrl) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String params = "{instance=\""+ instanceUrl + "\",mode=\"idle\"}";
        String query = restTemplate.getForObject(
                "http://35.247.41.79:9090/api/v1/query?query="
                + "1-avg(irate(node_cpu_seconds_total{instance}[2m]))", 
                String.class, params);
        JsonNode node = new ObjectMapper().readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public static Double getMemUsage(String instanceUrl) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        // {instance="URL"}
        String params = "%7Binstance%3D%22"+ instanceUrl + "%22%7D";
        // Plus signs not properly encoded, must use builder without encoding.
        // Rest template will automatically encode, cannot use.
        // If a URI is provided, Rest Template will not encode automatically.
        // URI builders do no encode plus signs properly either, must manually provide encoded URI.
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://35.247.41.79:9090/api/v1/query?"
                + "query=1-sum(node_memory_MemFree_bytes"
                + params
                + "%2Bnode_memory_Cached_bytes"
                + params
                + "%2Bnode_memory_Buffers_bytes"
                + params
                + ")/sum(node_memory_MemTotal_bytes"
                + params
                + ")")
                .build(true);
        URI uri = uriComponents.toUri();
        String query = restTemplate.getForObject(uri, String.class);
        JsonNode node = new ObjectMapper().readTree(query);
        return node.get("data").get("result").get(0).get("value").get(1).asDouble();
    }
    
    public static Double getNetUsage(String instanceUrl) throws IOException {
        
        return null;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println(getMemUsage("35.233.197.146:9090"));
    }
}
