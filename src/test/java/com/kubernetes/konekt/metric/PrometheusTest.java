package com.kubernetes.konekt.metric;

import io.kubernetes.client.ApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PrometheusTest {

    @Autowired
    Prometheus prometheus;

    @Test
    public void createAdditionalConfigsSecret() throws IOException, ApiException {
        prometheus.createAdditionalConfigsSecret();
    }
}