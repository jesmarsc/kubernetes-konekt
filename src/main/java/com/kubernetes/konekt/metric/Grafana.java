package com.kubernetes.konekt.metric;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.kubernetes.client.ApiException;

@Component
public class Grafana {
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    RestTemplate restTemplate;
    
    private static final String masterGrafana = "104.198.3.94:3000";
    
    private static final String token = "Bearer eyJrIjoiUDFyNUtLelBSSDQ2dTRYaGFCUFhpMGZ5YjZEOVdtWlYiLCJuIjoiU3ByaW5nIiwiaWQiOjF9";
    
    public String createDashboard(String username) throws JsonParseException, JsonMappingException, IOException {
        FileReader fileReader = new FileReader("dashboard.json");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, token);
        
        JsonNode jsonNode = objectMapper.readTree(fileReader);
        ((ObjectNode) jsonNode.get("dashboard")).put("title", username);
        String json = objectMapper.writeValueAsString(jsonNode);
        
        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
        
        String query = restTemplate.postForObject("http://"+ masterGrafana + "/api/dashboards/db", entity, String.class);
        JsonNode node = objectMapper.readTree(query);
        return node.get("url").asText();
    }
    
    public void addInstace(String instanceIp, String uid) throws IOException {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, token);
        
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange("http://" + masterGrafana + "/api/dashboards/uid/" + uid, 
                HttpMethod.GET, entity, String.class);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String query = jsonNode.get("dashboard").get("templating").get("list").get(1).get("query").asText();
        ArrayList<String> queryList = new ArrayList<String>(Arrays.asList(query.split(",")));
        queryList.add(instanceIp);
        ((ObjectNode) jsonNode.get("dashboard").get("templating").get("list").get(1)).put("query", String.join(", ", queryList));
        ((ObjectNode) jsonNode.get("dashboard")).put("overwrite", true);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
        
        String json = objectMapper.writeValueAsString(jsonNode);
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<String>(json, headers);
        restTemplate.postForObject("http://" + masterGrafana + "/api/dashboards/db", entity, String.class);
        
    }
    
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) throws IOException, ApiException {
        Grafana test = new Grafana();
        test.setObjectMapper(new ObjectMapper());
        test.setRestTemplate(new RestTemplate());
        String uid = test.createDashboard("Global Metrics");
        System.out.println(uid);
    }
}
