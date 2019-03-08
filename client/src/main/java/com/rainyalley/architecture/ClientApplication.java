package com.rainyalley.architecture;

import com.rainyalley.architecture.client.Result;
import com.rainyalley.architecture.client.Schedule;
import com.rainyalley.architecture.client.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@Slf4j
public class ClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class, args);
        Result result = context.getBean(Timer.class).schedule(new Schedule("test", "demo", "* 0/10 * * * ?"));
        log.info(result.toString());
    }
}
