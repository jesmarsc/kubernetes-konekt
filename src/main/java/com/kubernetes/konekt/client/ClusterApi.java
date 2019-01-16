package com.kubernetes.konekt.client;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
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
	/*
	 * Parameters
	 * MultipartFile file: deployment file 
	 * String url: cluster url
	 * String userName: username to access cluster
	 * String passWord: password for username provided
	 * String namespace: namespace are used to seperate accounts. username of the person logged in will be used as the namespace.
	 */
	public String execYaml(MultipartFile file, String url, String userName, String passWord, String namespace) throws IOException {
		FileReader fr = new FileReader(file.getOriginalFilename());
		InputStream input = file.getInputStream();
		Map map = (Map) yaml.load(input);
	    V1Deployment body = (V1Deployment) convertyamlToObject(fr, (String) map.get("kind"));
		
        ApiClient client = Config.fromUserPassword(url, userName, passWord, false);
        client.setDebugging(true);
        
        Configuration.setDefaultApiClient(client);
        AppsV1Api apiInstance = new AppsV1Api(client);
        
        String pretty = "true";
        V1Deployment result = null;
        
        try {
            result = apiInstance.createNamespacedDeployment(namespace, body, pretty);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AppsV1Api#createNamespacedDeployment");
            e.printStackTrace();
            return null;
        }
        return result.getMetadata().getName();
	}
	
	public static Object convertyamlToObject(FileReader fr, String kind) {
		return yaml.loadAs(fr, (Class<Object>) objMap.get(kind));
	}
	
	public Boolean deleteDeployment(String deploymentName, String url, String userName, String passWord, String username ) {

		// Configure API authorization: using username and password
		ApiClient client = Config.fromUserPassword(url, userName, passWord, false);
        client.setDebugging(true);
        Configuration.setDefaultApiClient(client);

		AppsV1Api apiInstance = new AppsV1Api();
		String namespace = username; // String | object name and auth scope, such as for teams and projects
		V1DeleteOptions body = new V1DeleteOptions(); // V1DeleteOptions | 
		String pretty = "true"; // String | If 'true', then the output is pretty printed.
		Integer gracePeriodSeconds = 56; // Integer | The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
		Boolean orphanDependents = true; // Boolean | Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the \"orphan\" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
		String propagationPolicy = "Orphan"; // String | Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
		try {
		
		    //V1Status result = 	// use to debug
		    apiInstance.deleteNamespacedDeployment(deploymentName, namespace, body, pretty, gracePeriodSeconds, orphanDependents, propagationPolicy);
		    //System.out.println(result);	// use to debug
		} catch (ApiException e) {
		    System.err.println("Exception when calling AppsV1Api#deleteNamespacedDeployment");
		    e.printStackTrace();
		    return false;
		}
		return true;
	}
	
	public void DeleteNamespace( String url, String username, String password, String namespace ) {
	
		// Configure API authorization: using username and password
				ApiClient client = Config.fromUserPassword(url, username, password, false);
		        client.setDebugging(true);
		        Configuration.setDefaultApiClient(client);
				
				CoreV1Api apiInstance = new CoreV1Api();
				V1DeleteOptions body = new V1DeleteOptions(); // V1DeleteOptions | 
				String pretty = "false"; // String | If 'true', then the output is pretty printed.
				String propagationPolicy = "Orphan"; // String | Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
				try {
					// The following call "apiInstance.deleteNamespaceWithHttpInfo" is known to throw an exception
					// https://github.com/kubernetes-client/java/issues/86
					// When the function is called it does delete the namespace even though the exception is thrown
					// There is no fix yet. The only solution is to make the call and catch the exception and move on.
				    ApiResponse<V1Status> response = apiInstance.deleteNamespaceWithHttpInfo(namespace, body, pretty, null, null, propagationPolicy);
				    
				     V1Status result = response.getData();
				   System.out.println(result);
				} catch (ApiException e) {
				    System.err.println("Exception when calling CoreV1Api#deleteNamespace");
				    e.printStackTrace();
				}
	}
	
	public Boolean NamespaceEmpty( String url, String userName, String passWord, String namespace ) {
		// Configure API authorization: using username and password
		ApiClient client = Config.fromUserPassword(url, userName, passWord, false);
        client.setDebugging(true);
        Configuration.setDefaultApiClient(client);
		
		CoreV1Api apiInstance = new CoreV1Api();
		String pretty = "true"; // String | If 'true', then the output is pretty printed.
		try {
		    ApiResponse<V1PodList> response = apiInstance.listNamespacedPodWithHttpInfo(namespace, pretty, null, null, null, null, null, null, null, null);
		    List<V1Pod> list = response.getData().getItems();
		    if(list.isEmpty()) {
		    	return true;
		    }
		    System.out.println(list);
		} catch (ApiException e) {
		    System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
		    e.printStackTrace();
		}

		return false;
	}

	public Boolean CheckNamespaceAlreadyExist(String namespace, String url, String userName, String passWord) {

		// Configure API authorization: using username and password

		ApiClient client = Config.fromUserPassword(url, userName, passWord, false);
        client.setDebugging(true);
        Configuration.setDefaultApiClient(client);
		CoreV1Api apiInstance = new CoreV1Api();
		String pretty = "true"; // String | If 'true', then the output is pretty printed.
		try {
		    ApiResponse<V1NamespaceList> response = apiInstance.listNamespaceWithHttpInfo(pretty, null, null, null, null, null, null, null, null);
		    V1NamespaceList result = response.getData();
		    List<V1Namespace> list = result.getItems();
		    for(V1Namespace item : list) {	
		    	if(item.getMetadata().getName().equals(namespace)) {
		    		return true; // namespace already exists.
		    	}
		    }
		} catch (ApiException e) {
		    System.err.println("Exception when calling CoreV1Api#listNamespace");
		    e.printStackTrace();
		}
		return false; // reached end of list without finding namespace
	}

	public void createNamespace(String namespace, String url, String userName, String passWord) {
		// Configure API authorization: using username and password
		ApiClient client = Config.fromUserPassword(url, userName, passWord, false);
        client.setDebugging(true);
        Configuration.setDefaultApiClient(client);
        
		CoreV1Api apiInstance = new CoreV1Api();
		V1Namespace body = new V1Namespace(); // V1Namespace |
		V1ObjectMeta metadata = new V1ObjectMeta();
		metadata.setName(namespace);
		body.setMetadata(metadata);
		String pretty = "true"; // String | If 'true', then the output is pretty printed.
		try {
		    //V1Namespace result =		// use to debug 
		    apiInstance.createNamespace(body, pretty);
		    //System.out.println(result);	// use to debug
		} catch (ApiException e) {
		    System.err.println("Exception when calling CoreV1Api#createNamespace");
		    e.printStackTrace();
		}		
	}
}
