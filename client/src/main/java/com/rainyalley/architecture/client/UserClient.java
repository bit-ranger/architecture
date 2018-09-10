package com.rainyalley.architecture.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "web")
public interface UserClient {

    @RequestMapping(value = "/api/v1/user/{id}",method = RequestMethod.GET)
    String get(@PathVariable("id") String id);


}
