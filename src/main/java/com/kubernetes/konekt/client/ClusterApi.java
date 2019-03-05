package com.kubernetes.konekt.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kubernetes.konekt.entity.Cluster;
import com.kubernetes.konekt.entity.Container;
import com.kubernetes.konekt.form.YamlBuilderForm;
import com.kubernetes.konekt.metric.Prometheus;
import com.kubernetes.konekt.security.ClusterSecurity;
import com.kubernetes.konekt.service.AccountService;
import com.kubernetes.konekt.service.ClusterService;
import com.kubernetes.konekt.service.ContainerService;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.ApiextensionsV1beta1Api;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.AppsV1beta2Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.CustomObjectsApi;
import io.kubernetes.client.apis.RbacAuthorizationV1Api;
import io.kubernetes.client.models.V1ClusterRole;
import io.kubernetes.client.models.V1ClusterRoleBinding;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.models.V1ConfigMapList;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1DeploymentList;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Role;
import io.kubernetes.client.models.V1RoleBinding;
import io.kubernetes.client.models.V1Secret;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceAccount;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.models.V1beta1CustomResourceDefinition;
import io.kubernetes.client.models.V1beta2DaemonSet;
import io.kubernetes.client.models.V1beta2Deployment;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;

@Component
public class ClusterApi {

    private ApiClient client;

    private CoreV1Api coreInstance;

    private AppsV1Api appsInstance;

    private ApiextensionsV1beta1Api apiExtensionsInstance;

    private CustomObjectsApi customObjectsInstance;

    private RbacAuthorizationV1Api rbacAuthApi;

    private AppsV1beta2Api appsBeta2Api;

    private static String pretty = "true";

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClusterSecurity clusterSecurity;

    private Boolean settingPrometheus = false;

    public ClusterApi() {

    }

    public ClusterApi(String clusterUrl, String clusterUser, String clusterPass) {
        client = Config.fromUserPassword(clusterUrl, clusterUser, clusterPass, false);
        client.setDebugging(false); // watches do not work if set to true
        client.setBasePath(clusterUrl);
        Configuration.setDefaultApiClient(client);
        coreInstance = new CoreV1Api(client);
        appsInstance = new AppsV1Api(client);
        customObjectsInstance = new CustomObjectsApi(client);
        apiExtensionsInstance = new ApiextensionsV1beta1Api(client);
        rbacAuthApi = new RbacAuthorizationV1Api();
        appsBeta2Api = new AppsV1beta2Api();
    }

    public void setupClient(String clusterUrl, String clusterUser, String clusterPass) {
        client = Config.fromUserPassword(clusterUrl, clusterUser, clusterPass, false);
        client.setDebugging(false);	// watches do not work if set to true
        client.setBasePath(clusterUrl);
        Configuration.setDefaultApiClient(client);
        coreInstance = new CoreV1Api(client);
        appsInstance = new AppsV1Api(client);
        customObjectsInstance = new CustomObjectsApi(client);
        apiExtensionsInstance = new ApiextensionsV1beta1Api(client);
        rbacAuthApi = new RbacAuthorizationV1Api();
        appsBeta2Api = new AppsV1beta2Api();
    }

    public List<Container> parseYaml(File file, String namespace, Long providerId) throws IOException, ApiException   {
        FileReader yamlReader = new FileReader(file);
        List<Object> objects = Yaml.loadAll(yamlReader);
        List<Container> result = new ArrayList<Container>();
        String resource;

        for (Object body : objects) {
            try {
                if (body instanceof V1Deployment) {
                    namespace = settingPrometheus ? ((V1Deployment) body).getMetadata().getNamespace() : namespace;
                    V1Deployment response = createDeploymentV1(namespace, (V1Deployment) body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "Deployment", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                } else if (body instanceof V1Service) {
                    namespace = settingPrometheus ? ((V1Service) body).getMetadata().getNamespace() : namespace;
                    V1Service response = createService(namespace, (V1Service) body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "Service", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));

                } else if (body instanceof V1ConfigMap) {
                    namespace = settingPrometheus ? ((V1ConfigMap) body).getMetadata().getNamespace() : namespace;
                    V1ConfigMap response = createConfigMap(namespace, (V1ConfigMap) body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "ConfigMap", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                } else if (body instanceof V1beta1CustomResourceDefinition) {
                    namespace = settingPrometheus ? ((V1beta1CustomResourceDefinition) body).getMetadata().getNamespace() : namespace;
                    V1beta1CustomResourceDefinition response = createCustomResourceDefinition((V1beta1CustomResourceDefinition) body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "CustomResourceDefinitions", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }else if (body instanceof V1ClusterRole ) {
                    namespace = settingPrometheus ? ((V1ClusterRole) body).getMetadata().getNamespace() : namespace;
                    V1ClusterRole response = createClusterRole((V1ClusterRole) body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "ClusterRole", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }else if (body instanceof V1ClusterRoleBinding ) {
                    namespace = settingPrometheus ? ((V1ClusterRoleBinding) body).getMetadata().getNamespace() : namespace;
                    V1ClusterRoleBinding response = createClusterRoleBinding((V1ClusterRoleBinding) body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "ClusterRoleBinding", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }else if(body instanceof V1ServiceAccount) {
                    namespace = settingPrometheus ? ((V1ServiceAccount) body).getMetadata().getNamespace() : namespace;
                    V1ServiceAccount response =createServiceAccount(namespace,(V1ServiceAccount)body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "ServiceAccount", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }else if(body instanceof V1Role){
                    namespace = settingPrometheus ? ((V1Role) body).getMetadata().getNamespace() : namespace;
                    V1Role response = createRole(namespace,(V1Role)body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "Role", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }else if(body instanceof V1beta2DaemonSet){
                    namespace = settingPrometheus ? ((V1beta2DaemonSet) body).getMetadata().getNamespace() : namespace;
                    V1beta2DaemonSet response = createDaemonSet(namespace, (V1beta2DaemonSet)body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "V1beta2DaemonSet", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }else if(body instanceof V1Namespace && settingPrometheus) {
                    namespace = ((V1Namespace)body).getMetadata().getName();
                    createNamespace(namespace);
                }else if(body instanceof V1beta2Deployment) {
                    namespace = settingPrometheus ? ((V1beta2Deployment) body).getMetadata().getNamespace() : namespace;
                    V1beta2Deployment response = createDeploymentV1Beta2(namespace, (V1beta2Deployment)body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "V1beta2Deployment", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }else if(body instanceof V1RoleBinding ) {
                    namespace = settingPrometheus ? ((V1RoleBinding) body).getMetadata().getNamespace() : namespace;
                    V1RoleBinding response = createNamespacedRoleBinding(namespace, (V1RoleBinding) body);
                    resource = response.getMetadata().getName();
                    result.add(new Container(resource, "V1RoleBinding", "Running", client.getBasePath(), providerId,response.getMetadata().getUid()));
                }
            }catch(Exception e ) {
                if(settingPrometheus) {
                    e.printStackTrace();
                    continue;
                }else {
                    return null;
                }
            }

        }

        return result;
    }

    public File saveFileLocally(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public void setupPrometheus(Long providerId, String url, String user, String pass) throws ApiException, IOException {
        
        settingPrometheus = true;
        
        if(!this.checkNamespaceAlreadyExist("monitoring")) {
    
            String filePath = "manifests/ultimate-prometheus-setup.yaml";
            String namespace = "monitoring";
    
            File bigYamlFile = new File(filePath);
            parseYaml(bigYamlFile, namespace,  providerId);
    
            //run all yaml files in custom-objects directory
            String directoryPath = "manifests/custom-objects/";
            File folder = new File(directoryPath);
    
            for(File file : folder.listFiles()) {
                if(file.isFile() && !file.isHidden()) {
                    filePath = directoryPath + file.getName();
                    FileReader fr = new FileReader(filePath);
                    @SuppressWarnings("rawtypes")
                    Map customObjectMap = Yaml.loadAs(fr, Map.class);
                    if(customObjectMap.containsValue("ServiceMonitor")) {
                        createServiceMonitor(customObjectMap);
                    }
                    if(customObjectMap.containsValue("Prometheus")) {
                        createPrometheus(customObjectMap);
                    }
                    if(customObjectMap.containsValue("PrometheusRule")) {
                        createPrometheusRule(customObjectMap);
                    }
                }
            }
            Prometheus prometheus = new Prometheus();
            prometheus.addCluster(url.substring(8), user, pass);
        }

        settingPrometheus = false;
    }

    public V1ServiceList getNamespacedV1ServiceList(String namespace) throws ApiException {
        V1ServiceList result = coreInstance.listNamespacedService(namespace, null, null,"", Boolean.FALSE,  null, null, null, null, Boolean.FALSE);
        return result;
    }

    public V1DeploymentList getNamespacedV1DeploymentList(String namespace) throws ApiException {
        V1DeploymentList result = appsInstance.listNamespacedDeployment(namespace, pretty, null, null, Boolean.FALSE, null, null, null, null, Boolean.FALSE);
        return result;
    }

    public String getStatusByKindAndUid(String namespace, String kind, String uid ) throws ApiException {

        if(kind.equals("Service")) {
            V1ServiceList result = getNamespacedV1ServiceList(namespace);
            System.out.println(result);
            for(V1Service item : result.getItems()) {
                if(item.getMetadata().getUid().equals(uid)) {
                    return item.toString();
                }
            }
        }

        if(kind.equals("Deployment")) {
            V1DeploymentList result = getNamespacedV1DeploymentList(namespace);
            System.out.println(result);
            for(V1Deployment item : result.getItems()) {
                System.out.println(item.getMetadata().getUid());
                if(item.getMetadata().getUid().equals(uid)) {
                    return item.toString();
                }
            }
        }

        return new String();
    }

    public void createPrometheus(@SuppressWarnings("rawtypes") Map prometheusMap) throws ApiException, IOException {
        customObjectsInstance.createNamespacedCustomObject("monitoring.coreos.com", "v1", 
                "monitoring", "prometheuses", prometheusMap, pretty);
    }

    public void createServiceMonitor(@SuppressWarnings("rawtypes") Map serviceMonitorMap) throws ApiException, IOException {
        customObjectsInstance.createNamespacedCustomObject("monitoring.coreos.com", "v1", 
                "monitoring", "servicemonitors", serviceMonitorMap, pretty);
    }

    public void createPrometheusRule(@SuppressWarnings("rawtypes") Map prometheusRulesMap) throws ApiException, IOException {
        customObjectsInstance.createNamespacedCustomObject("monitoring.coreos.com", "v1", 
                "monitoring", "prometheusrules", prometheusRulesMap, pretty);
    }

    public V1beta2DaemonSet createDaemonSet(String namespace, V1beta2DaemonSet body) throws ApiException {
        V1beta2DaemonSet result = appsBeta2Api.createNamespacedDaemonSet(namespace, body, pretty);
        return result;
    }

    public V1Role createRole(String namespace, V1Role body) throws ApiException {
        V1Role result = rbacAuthApi.createNamespacedRole(namespace, body, pretty);
        return result;
    }

    public V1ServiceAccount createServiceAccount(String namespace,V1ServiceAccount body) throws ApiException {
        V1ServiceAccount result = coreInstance.createNamespacedServiceAccount(namespace, body, pretty);
        return result;
    }

    public V1ClusterRoleBinding createClusterRoleBinding(V1ClusterRoleBinding body) throws ApiException {
        V1ClusterRoleBinding result = rbacAuthApi.createClusterRoleBinding(body, pretty);
        return result;
    }

    public V1RoleBinding createNamespacedRoleBinding(String namespace,V1RoleBinding body) throws ApiException {
        V1RoleBinding result = null;
        result = rbacAuthApi.createNamespacedRoleBinding(namespace, body, pretty);       
        return result;
    }

    public V1ClusterRole createClusterRole(V1ClusterRole body) throws ApiException {
        V1ClusterRole result = rbacAuthApi.createClusterRole(body, pretty);
        return result;
    }

    public V1Deployment createDeploymentV1(String namespace, V1Deployment body) throws ApiException {
        V1Deployment result = null;
        result = appsInstance.createNamespacedDeployment(namespace, body, pretty);
        return result;        
    }

    public V1beta2Deployment createDeploymentV1Beta2(String namespace, V1beta2Deployment body) throws ApiException {
        V1beta2Deployment result = null;
        result = appsBeta2Api.createNamespacedDeployment(namespace, body, pretty);
        return result;
    }

    public V1Service createService(String namespace, V1Service body) throws ApiException {
        V1Service result = null;
        result = coreInstance.createNamespacedService(namespace, body, pretty);
        return result;
    }

    public V1ConfigMap createConfigMap(String namespace, V1ConfigMap body) throws ApiException {
        V1ConfigMap result = null;
        result = coreInstance.createNamespacedConfigMap(namespace, body, pretty);
        return result;
    }

    public V1beta1CustomResourceDefinition createCustomResourceDefinition
    (V1beta1CustomResourceDefinition body) throws ApiException {
        V1beta1CustomResourceDefinition result = null;
        result = apiExtensionsInstance.createCustomResourceDefinition(body, pretty);
        return result;
    }

    public V1Secret createSecret(String namespace, V1Secret body) throws ApiException {
        V1Secret result = null;
        result = coreInstance.createNamespacedSecret(namespace, body, pretty);
        return result;
    }

    public V1Secret replaceSecret(String name, String namespace, V1Secret body) throws ApiException {
        V1Secret result = null;
        result = coreInstance.replaceNamespacedSecret(name, namespace, body, pretty);
        return result;
    }

    public void deleteDeployment(String namespace, String deploymentName) throws ApiException {
        V1DeleteOptions body = new V1DeleteOptions(); // V1DeleteOptions |
        appsInstance.deleteNamespacedDeployment(deploymentName, namespace, body, pretty, null, null, null);
    }

    public void deleteService(String namespace, String serviceName) throws ApiException {
        V1DeleteOptions body = new V1DeleteOptions();
        coreInstance.deleteNamespacedService(serviceName, namespace, body, pretty, null, null, null);
    }

    public void deleteConfigMap(String namespace, String configName) throws ApiException {
        V1DeleteOptions body = new V1DeleteOptions();
        coreInstance.deleteNamespacedConfigMap(configName, namespace, body, pretty, null, null, null);
    }

    public void deleteNamespace(String namespace) throws ApiException {

        V1DeleteOptions body = new V1DeleteOptions();

        // The following call "apiInstance.deleteNamespaceWithHttpInfo" is known to
        // throw an exception
        // https://github.com/kubernetes-client/java/issues/86
        // When the function is called it does delete the namespace even though the
        // exception is thrown
        // There is no fix yet. The only solution is to make the call and catch the
        // exception and move on.
        //ApiResponse<V1Status> response = 	// For debugging to read response
        coreInstance.deleteNamespaceWithHttpInfo(namespace, body, pretty, null, null, null);
        //V1Status result = response.getData(); // For debugging

    }

    public Boolean namespaceEmpty(String namespace) throws ApiException {

        ApiResponse<V1PodList> response = 
                coreInstance.listNamespacedPodWithHttpInfo(namespace, pretty, 
                        null, null, null, null, null, null, null, null);

        List<V1Pod> list = response.getData().getItems();
        if (list.isEmpty()) {
            return true;
        }

        return false;
    }

    public Boolean checkNamespaceAlreadyExist(String namespace) throws ApiException {

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

        return false; // reached end of list without finding namespace
    }

    public void createNamespace(String namespace) throws ApiException {

        V1Namespace body = new V1Namespace(); // V1Namespace |
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(namespace);
        body.setMetadata(metadata);
        coreInstance.createNamespace(body, pretty);

    }

    public List<String> getDeploymentsByNamespace(String namespace) throws ApiException {

        ApiResponse<V1DeploymentList> response = 
                appsInstance.listNamespacedDeploymentWithHttpInfo(namespace, pretty, 
                        null, null, null, null, null, null, null, null);

        List<V1Deployment> result = response.getData().getItems();
        List<String> deploymentNames = new ArrayList<String>();
        for (V1Deployment item : result) {
            deploymentNames.add(item.getMetadata().getName());
        }

        return deploymentNames;
    }

    public void checkUserWorkload(List<Container> containers) {
        Cluster cluster = null;
        String username = null;

        for(Container container : containers) {
            cluster = clusterService.getCluster(container.getClusterUrl());
            username = container.getAccount().getUserName();
            Boolean found = false;
            Blob encryptedUsername = cluster.getEncryptedUsername();
            Blob encryptedPassword = cluster.getEncryptedPassword();
            String clusterUsername = clusterSecurity.decodeCredential(encryptedUsername);
            String clusterPassword	= clusterSecurity.decodeCredential(encryptedPassword);
            setupClient(cluster.getClusterUrl(), clusterUsername , clusterPassword);
            try {
                if (container.getKind().equals("Deployment")) {
                    // get list of deployments running on namespace
                    ApiResponse<V1DeploymentList> response = appsInstance.listNamespacedDeploymentWithHttpInfo(username,
                            pretty, null, null, null, null, null, null, null, null);
                    List<V1Deployment> results = response.getData().getItems();
                    // check if deployment is on cluster  
                    for (Iterator<V1Deployment> iterator = results.iterator(); iterator.hasNext() && !found;) {
                        if(iterator.next().getMetadata().getName().equals(container.getContainerName())) {
                            found = true;
                        }
                    }

                } else if (container.getKind().equals("Service")) {
                    // get list of services running on namespace
                    ApiResponse<V1ServiceList> response = coreInstance.listNamespacedServiceWithHttpInfo(username,
                            pretty, null, null, null, null, null, null, null, null);
                    List<V1Service> results = response.getData().getItems();
                    // check if Service is on cluster
                    for (Iterator<V1Service> iterator = results.iterator(); iterator.hasNext() && !found;) {
                        if(iterator.next().getMetadata().getName().equals(container.getContainerName())) {
                            found = true;
                        }
                    }

                } else if (container.getKind().equals("ConfigMap")) {
                    // get list of config maps in namespace
                    ApiResponse<V1ConfigMapList> response = coreInstance.listNamespacedConfigMapWithHttpInfo(username,
                            pretty, null, null, null, null, null, null, null, null);
                    List<V1ConfigMap> results = response.getData().getItems();
                    // check if config map  is in namespace
                    for (Iterator<V1ConfigMap> iterator = results.iterator(); iterator.hasNext() && !found;) {
                        if(iterator.next().getMetadata().getName().equals(container.getContainerName())) {
                            found = true;
                        }
                    }
                }
            } catch (ApiException e) {

            }
            if(!found) {
                containerService.deleteContainer(container);
                accountService.updateAccountTables(container.getAccount());
            }
        }
    }

    public List<Container> deploymentFromUserInput(String namespace, YamlBuilderForm form, Long providerId) throws IOException, ApiException {

        String tab = "  ";
        String label = form.getKey() + ": " + form.getValue();
        String fileContent =
                "apiVersion: apps/v1\n" 
                        + "kind: Deployment\n"
                        + "metadata:\n"
                        + tab + "name: " + form.getDeploymentName() + "\n"
                        + tab + "labels:\n"
                        + tab + tab + label + "\n"
                        + "spec:\n"
                        + tab + "replicas: " + form.getReplicas() + "\n"
                        + tab + "selector:\n"
                        + tab + tab + "matchLabels:\n"
                        + tab + tab + tab + label + "\n"
                        + tab + "template:\n"
                        + tab + tab + "metadata:\n"
                        + tab + tab + tab + "labels:\n"
                        + tab + tab + tab + tab + label + "\n"
                        + tab + tab + "spec:\n"
                        + tab + tab + tab + "containers:\n"
                        + tab + tab + tab + "- name: " + form.getValue() + "\n"
                        + tab + tab + tab + tab + "image: " + form.getImage() + "\n"
                        + tab + tab + tab + tab + "ports:\n"
                        + tab + tab + tab + tab + "- containerPort: " + form.getContainerPort();

        String fileName = form.getDeploymentName() + ".yaml";
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        return parseYaml(file, namespace, providerId);
    }

    /*
    private MultipartFile convertMultipartFile(String fileName, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String contentType = "text/plain";
        byte[] content = null;
        content = Files.readAllBytes(path);
        MultipartFile readFile = new MockMultipartFile(fileName, filePath, contentType, content);
        return readFile;
    }

    public void setWatch(String url, String user, String pass) throws ApiException {

        Thread thread = new Thread(checkServices(url,user,pass));
        thread.start();

    }

    public Runnable checkServices(String curl, String cuser, String cpass) {

        return new Runnable() {
            private Boolean shutDown = false;
            private String url = curl;
            private String user = cuser;
            private String pass = cpass;
            public void run() {
                while (!shutDown)
                {
                    setupClient(url, user, pass);
                    V1ServiceList result;
                    try {
                        result = getNamespacedV1ServiceList("monitoring");
                        for(V1Service item :result.getItems()) {
                            if(item.getStatus().getLoadBalancer().getIngress() != null && item.getMetadata().getName().equals("prometheus-k8s")) {

                                Cluster cluster = clusterService.getCluster(url);
                                Account account = cluster.getAccount();
                                List<Cluster> list = account.getClusters();
                                list.remove(cluster);

                                String prometheusIp = item.getStatus().getLoadBalancer().getIngress().get(0).getIp() + ":9090";

                                // update cluster 
                                // TODO: add logic to add cluster to master 
                                prometheus.addPrometheusInstance(prometheusIp);
                                list.add(cluster);
                                account.setClusters(list);
                                // set cluster status to Ready
                                cluster.setStatus("Ready");
                                accountService.updateAccountTables(account);
                                // shutdown thread
                                shutDown = true;
                            }
                        }
                    } catch (ApiException | IOException e) {

                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000 * 10);

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }   
            }
        };
    }
     */
}
