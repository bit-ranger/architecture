package com.rainyalley.architecture.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableHystrix
@EnableDiscoveryClient
@Configuration
public class ClientConfig {

    @Bean("restTemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
