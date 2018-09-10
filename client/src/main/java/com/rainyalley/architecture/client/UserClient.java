package com.rainyalley.architecture.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class UserClient {

    private RestTemplate restTemplate;

    public String actuator() {
        return restTemplate.getForObject("http://WEB//actuator", String.class);
    }

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
