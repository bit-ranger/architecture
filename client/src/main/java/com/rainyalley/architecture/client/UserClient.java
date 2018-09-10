package com.rainyalley.architecture.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class UserClient {

    private RestTemplate restTemplate;

    public String get(String id) {
        return restTemplate.getForObject("http://WEB/api/v1/user/" + id, String.class);
    }

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
