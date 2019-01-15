package com.kubernetes.konekt;
// Import classes:
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.models.V1Deployment;

public class TestBearerTokenConnection {

	public static void main(String[] args) {


		ApiClient defaultClient = Configuration.getDefaultApiClient();

		// Configure API key authorization: BearerToken
		ApiKeyAuth BearerToken = (ApiKeyAuth) defaultClient.getAuthentication("BearerToken");
		BearerToken.setApiKey("YOUR API KEY");
		// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
		//BearerToken.setApiKeyPrefix("Token");

		AppsV1Api apiInstance = new AppsV1Api();
		String namespace = "namespace_example"; // String | object name and auth scope, such as for teams and projects
		V1Deployment body = new V1Deployment(); // V1Deployment | 
		Boolean includeUninitialized = true; // Boolean | If true, partially initialized resources are included in the response.
		String pretty = "pretty_example"; // String | If 'true', then the output is pretty printed.
		String dryRun = "dryRun_example"; // String | When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
		try {
		    V1Deployment result = apiInstance.createNamespacedDeployment(namespace, body, pretty);
		    System.out.println(result);
		} catch (ApiException e) {
		    System.err.println("Exception when calling AppsV1Api#createNamespacedDeployment");
		    e.printStackTrace();
		}
	}

}
