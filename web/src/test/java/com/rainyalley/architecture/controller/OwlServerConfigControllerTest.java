package com.rainyalley.architecture.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


@RunWith(SpringRunner.class)
@Slf4j
public class OwlServerConfigControllerTest {

    CountDownLatch latch = new CountDownLatch(1);
    private Map<String, HttpStatus> result = new HashMap<>();

    @Test
    public void postStream() throws Exception{

        WebClient webClient = WebClient.create("http://localhost:8081");

        webClient.get()
                .uri("/v1/config?id=92")
                .cookie("OWL_SERVER_SESSION_ID", "OTM4ZDZmZjYtOTA0OS00YTYzLTg5M2EtNjEzYmIzOThiMWM0")
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(u -> Flux.fromIterable((List<Map<String, Object>>) u.get("list")).doOnNext(
                        c -> resave(webClient, c)
                        ).subscribe()
                ).subscribe();
        latch.await();
        result.entrySet().forEach(
                System.out::println
        );
    }

    private void resave(WebClient webClient, Map<String,Object> config){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(config);
//        latch.countDown();
        Mono<Body> body = Mono.just(new Body(((Map<String, Object>) config.get("content")).get("content").toString()));
        webClient.put()
                .uri(String.format("/v1/config/%s", config.get("id").toString()))
                .cookie("OWL_SERVER_SESSION_ID", "OTM4ZDZmZjYtOTA0OS00YTYzLTg5M2EtNjEzYmIzOThiMWM0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body, Body.class)
                .retrieve()
                .onStatus(status -> true, resp -> {
                    result.put(config.get("id").toString(), resp.statusCode());
                    latch.countDown();
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .subscribe();
    }

    @AllArgsConstructor
    @Data
    private static class Body{

        private String content;

    }
}