package com.rainyalley.architecture.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "timer")
public interface Timer {

    @RequestMapping("/v1/timer/schedule")
    Result schedule(Schedule schedule);

}
