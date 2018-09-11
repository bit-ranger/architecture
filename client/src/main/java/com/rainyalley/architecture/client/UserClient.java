package com.rainyalley.architecture.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class UserClient {

    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallback")
    public String get(String id) {
        return restTemplate.getForObject("http://WEB/api/v1/user/" + id, String.class);
    }

    String getFallback(String id){
        return String.format("get(%s) fall back!", id);
    }


    @Resource(name = "restTemplate")
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
