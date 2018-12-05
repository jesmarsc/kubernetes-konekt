package com.kubernetes.konekt.client;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Config;

@Component
public class ClusterApi {
	static Yaml yaml = new Yaml();
	static Map<String,Object> objMap = new HashMap<>();
	static {
		objMap.put("Deployment", V1Deployment.class);
		objMap.put("Namespace",V1Namespace.class);
		objMap.put("Service", V1Service.class);
	}
	
	public String execYaml(MultipartFile file, String url, String userName, String passWord) throws IOException {
		FileReader fr = new FileReader(file.getOriginalFilename());
		InputStream input = file.getInputStream();
		Map map = (Map) yaml.load(input);
	    V1Deployment body = (V1Deployment) convertyamlToObject(fr, (String) map.get("kind"));
		
        ApiClient client = Config.fromUserPassword(url, userName, passWord, false);
        client.setDebugging(true);
        
        Configuration.setDefaultApiClient(client);
        AppsV1Api apiInstance = new AppsV1Api(client);
        
        String namespace = "default";
        String pretty = "true";
        V1Deployment result = null;
        
        try {
            result = apiInstance.createNamespacedDeployment(namespace, body, pretty);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AppsV1Api#createNamespacedDeployment");
            e.printStackTrace();
        }
        return result.getMetadata().getName();
	}
	
	public static Object convertyamlToObject(FileReader fr, String kind) {
		return yaml.loadAs(fr, (Class<Object>) objMap.get(kind));
	}
}
