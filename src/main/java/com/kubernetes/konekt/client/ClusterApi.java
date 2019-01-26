package com.kubernetes.konekt.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kubernetes.konekt.entity.Container;
import com.kubernetes.konekt.form.YamlBuilderForm;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1DeploymentList;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;

@Component
public class ClusterApi {

    private ApiClient client;

    private CoreV1Api coreInstance;

    private AppsV1Api appsInstance;

    private static String pretty = "true";

    public List<Container> parseYaml(MultipartFile file, String clusterUrl, 
            String clusterUser, String clusterPass, String namespace) throws IOException, ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);
        saveFileLocally(file); // save file in local directory so convertyamlToObject can find the file

        FileReader fr = new FileReader(file.getOriginalFilename());
        List<Object> objects = Yaml.loadAll(fr);
        List<Container> result = new ArrayList<Container>();
        String name = null;

        for (Object body : objects) {
            if (body instanceof V1Deployment) {
                name = createDeployment((V1Deployment) body, namespace).getMetadata().getName();
                result.add(new Container(name, clusterUrl, "Deployment", "Running"));
            } else if (body instanceof V1Service) {
                name = createService((V1Service) body, namespace).getMetadata().getName();
                result.add(new Container(name, clusterUrl, "Service", "Running"));
            } else if (body instanceof V1ConfigMap) {
                name = createConfigMap((V1ConfigMap) body, namespace).getMetadata().getName();
                result.add(new Container(name, clusterUrl, "ConfigMap", "Running"));
            }
        }

        return result;
    }

    public List<Container> deploymentFromUserInput(String clusterUrl, String clusterUser, String clusterPass,
            String namespace, YamlBuilderForm form) throws IOException, ApiException {

        String tab = "  ";
        String label = form.getKey() + ": " + form.getValue();
        String fileContent =
                "apiVersion: apps/v1 \n" 
              + "kind: Deployment \n"
              + "metadata: \n"
              + tab + "name: " + form.getDeploymentName() + "\n"
              + tab + "labels: \n"
              + tab + tab + label + "\n"
              + "spec: \n"
              + tab + "replicas: " + form.getReplicas() + "\n"
              + tab + "selector: \n"
              + tab + tab + "matchLabels: \n"
              + tab + tab + tab + label + "\n"
              + tab +"template: \n"
              + tab + tab + "metadata: \n"
              + tab + tab + tab + "labels: \n"
              + tab + tab + tab + tab + label + "\n"
              + tab + tab + "spec: \n"
              + tab + tab + tab + "containers: \n"
              + tab + tab + tab + "- name: " + form.getValue() + "\n"
              + tab + tab + tab + tab + "image: " + form.getImage() + "\n"
              + tab + tab + tab + tab + "ports: \n"
              + tab + tab + tab + tab + "- containerPort: " + form.getContainerPort();

        String fileName = form.getDeploymentName() + ".yaml";
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Path path = Paths.get(fileName);
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile readFile = new MockMultipartFile(fileName, fileName, contentType, content);

        return parseYaml(readFile, clusterUrl, clusterUser, clusterPass, namespace);
    }

    public void setupClient(String clusterUrl, String clusterUser, String clusterPass) {

        client = Config.fromUserPassword(clusterUrl, clusterUser, clusterPass, false);
        client.setDebugging(true);

        Configuration.setDefaultApiClient(client);
        coreInstance = new CoreV1Api(client);
        appsInstance = new AppsV1Api(client);

    }

    private File saveFileLocally(MultipartFile file) throws IOException {
        try {
            File convFile = new File(file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } catch (IOException e) {
            return null;
        }
    }

    public V1Deployment createDeployment(V1Deployment body, String namespace) throws ApiException {

        V1Deployment result = null;

        try {
            result = appsInstance.createNamespacedDeployment(namespace, body, pretty);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AppsV1Api#createNamespacedDeployment");
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    public V1Service createService(V1Service body, String namespace) throws ApiException {

        V1Service result = null;

        try {
            result = coreInstance.createNamespacedService(namespace, body, pretty);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespacedService");
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    public V1ConfigMap createConfigMap(V1ConfigMap body, String namespace) throws ApiException {

        V1ConfigMap result = null;

        try {
            result = coreInstance.createNamespacedConfigMap(namespace, body, pretty);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespacedConfigMap");
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    public void deleteDeployment(String deploymentName, String namespace, String clusterUrl, 
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);

        V1DeleteOptions body = new V1DeleteOptions(); // V1DeleteOptions |

        try {
            appsInstance.deleteNamespacedDeployment(deploymentName, namespace, body, pretty, null, null, null);
        } catch (ApiException e) {
            System.err.println("Exception when calling AppsV1Api#deleteNamespacedDeployment");
            e.printStackTrace();
            throw e;
        }

    }

    public void deleteService(String serviceName, String namespace, String clusterUrl, 
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);

        V1DeleteOptions body = new V1DeleteOptions();

        try {
            coreInstance.deleteNamespacedService(serviceName, namespace, body, pretty, null, null, null);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#deleteNamespacedService");
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteConfigMap(String configName, String namespace, String clusterUrl, 
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);

        V1DeleteOptions body = new V1DeleteOptions();

        try {
            coreInstance.deleteNamespacedConfigMap(configName, namespace, body, pretty, null, null, null);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#deleteNamespacedConfigMap");
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteNamespace(String namespace, String clusterUrl, 
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);

        V1DeleteOptions body = new V1DeleteOptions();

        try {
            // The following call "apiInstance.deleteNamespaceWithHttpInfo" is known to
            // throw an exception
            // https://github.com/kubernetes-client/java/issues/86
            // When the function is called it does delete the namespace even though the
            // exception is thrown
            // There is no fix yet. The only solution is to make the call and catch the
            // exception and move on.
            ApiResponse<V1Status> response = 
                    coreInstance.deleteNamespaceWithHttpInfo(namespace, body, pretty, null, null, null);
            V1Status result = response.getData();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#deleteNamespace");
            e.printStackTrace();
            throw e;
        }
    }

    public Boolean namespaceEmpty(String namespace, String clusterUrl, 
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);

        try {
            ApiResponse<V1PodList> response = 
                    coreInstance.listNamespacedPodWithHttpInfo(namespace, pretty, 
                            null, null, null, null, null, null, null, null);
            List<V1Pod> list = response.getData().getItems();
            if (list.isEmpty()) {
                return true;
            }
            System.out.println(list);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            e.printStackTrace();
            throw e;
        }

        return false;
    }

    public Boolean checkNamespaceAlreadyExist(String namespace, String clusterUrl, 
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);

        try {
            ApiResponse<V1NamespaceList> response = 
                    coreInstance.listNamespaceWithHttpInfo(pretty, 
                            null, null, null, null, null, null, null, null);
            V1NamespaceList result = response.getData();
            List<V1Namespace> list = result.getItems();
            for (V1Namespace item : list) {
                if (item.getMetadata().getName().equals(namespace)) {
                    return true; // namespace already exists.
                }
            }
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#listNamespace");
            e.printStackTrace();
            throw e;
        }
        return false; // reached end of list without finding namespace
    }

    public void createNamespace(String namespace, String clusterUrl,
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);

        V1Namespace body = new V1Namespace(); // V1Namespace |
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(namespace);
        body.setMetadata(metadata);

        try {
            coreInstance.createNamespace(body, pretty);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNamespace");
            e.printStackTrace();
            throw e;
        }
    }

    public List<String> getDeploymentsByNamespace(String namespace, String clusterUrl, 
            String clusterUser, String clusterPass) throws ApiException {

        setupClient(clusterUrl, clusterUser, clusterPass);
        ApiResponse<V1DeploymentList> response = 
                appsInstance.listNamespacedDeploymentWithHttpInfo(namespace, pretty, 
                        null, null, null, null, null, null, null, null);
        List<V1Deployment> result = response.getData().getItems();
        List<String> deploymentNames = new ArrayList<String>();
        for (V1Deployment item : result) {
            deploymentNames.add(item.getMetadata().getName());
        }
        System.out.println(deploymentNames);

        return deploymentNames;
    }
}
