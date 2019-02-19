package com.kubernetes.konekt;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class SpringMvcBootApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SpringMvcBootApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.build();
	}

	@Bean
	public ObjectMapper objectMapper() {
	    return new ObjectMapper();
	}
	 
}
