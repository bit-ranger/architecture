package com.rainyalley.architecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.po.UserAdd;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;


@RunWith(SpringRunner.class)
@Slf4j
public class UserControllerTest {


    @Test
    public void postStream() throws Exception{
        Flux<UserAdd> userAddFlux =
                Flux.interval(Duration.ofSeconds(1))
                .map(l -> new UserAdd(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis())))
                .take(5);

        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .post().uri("/v1/user")
                .contentType(MediaType.APPLICATION_STREAM_JSON) // 2
                .body(userAddFlux, UserAdd.class) // 3
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::map)
                .doOnNext(u -> log.debug(String.format("receive: %s", u)))
                .blockLast();
    }

    private User map(String s){
        ObjectMapper objectMapper  = new ObjectMapper();
        try {
            return objectMapper.readValue(s, User.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}