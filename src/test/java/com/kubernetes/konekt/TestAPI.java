package com.kubernetes.konekt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Config;

public class TestAPI {
	private static final String yaml_file = "helloworld.yaml";
	static Map<String,Object> objMap = new HashMap<>();
	static Yaml yaml = new Yaml();
	static {
		objMap.put("Deployment", V1Deployment.class);
		objMap.put("Namespace",V1Namespace.class);
		objMap.put("Service", V1Service.class);
	}
	
	public static void main(String[] args) throws IOException{
		FileReader fr = new FileReader(yaml_file);
	    InputStream input = new FileInputStream(new File(yaml_file));
	    @SuppressWarnings("rawtypes")
		Map map = (Map) yaml.load(input);
	    V1Deployment body = (V1Deployment) convertyamlToObject(fr, (String) map.get("kind"));

		
        ApiClient client = Config.fromUserPassword("https://35.236.79.102", "admin", "5jHOLz8jmznl1PTn", false);
        client.setDebugging(true);
        
        Configuration.setDefaultApiClient(client);
        AppsV1Api apiInstance = new AppsV1Api(client);
        
        String namespace = "default";
        String pretty = "true";
        
        try {
            V1Deployment result = apiInstance.createNamespacedDeployment(namespace, body, pretty);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AppsV1Api#createNamespacedDeployment");
            e.printStackTrace();
        }
    }
	
	@SuppressWarnings("unchecked")
	public static Object convertyamlToObject(FileReader fr, String kind) {
		return yaml.loadAs(fr, (Class<Object>) objMap.get(kind));
	}
}
