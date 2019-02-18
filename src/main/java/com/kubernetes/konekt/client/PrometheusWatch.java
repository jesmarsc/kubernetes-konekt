package com.kubernetes.konekt.client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.reflect.TypeToken;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;

public class PrometheusWatch {
	@Autowired
	private ClusterApi clusterApi;
	
	private Watch<V1Service> watch;
	
    private ApiClient client;
    private CoreV1Api coreV1Api;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	public PrometheusWatch()
    {
		
		try
        {
            client = Config.defaultClient();
            client.getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
            Configuration.setDefaultApiClient(client);
            coreV1Api = new CoreV1Api();
            watch = Watch.createWatch(
                client,
                coreV1Api.listPodForAllNamespacesCall(null, null, null, null, null, null,null,null,Boolean.TRUE,null, null),
                new TypeToken<Watch.Response<V1Service>>() {}.getType());
            executorService.execute(new WatchHandler(watch));
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
