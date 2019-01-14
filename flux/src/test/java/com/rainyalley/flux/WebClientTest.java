package com.rainyalley.flux;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WebClientTest {


    @Test
    public void webClientTest1() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");   // 1
        Mono<String> resp = webClient
                .get().uri("/hello") // 2
                .retrieve() // 3
                .bodyToMono(String.class);  // 4
        resp.subscribe(System.out::println);    // 5
        TimeUnit.SECONDS.sleep(1);  // 6
    }


    @Test
    public void webClientTest2() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");   // 1
        webClient
                .get()
                .uri("/coffee")
                .accept(MediaType.APPLICATION_JSON)// 2
                .exchange() // 3
                .flatMapMany(res -> res.bodyToFlux(Coffee.class))
                .doOnNext(System.out::println)
                .blockLast();
    }


    @Test
    public void webClientTest3() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .get().uri("/coffee")
                .accept(MediaType.APPLICATION_STREAM_JSON)   // 1
                .retrieve()
                .bodyToFlux(String.class)
                .log()  // 2
                .take(10)   // 3
                .blockLast();
    }




    @Test
    public void webClientTest4() {
        Random r = new Random();
        Flux<Coffee> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> new Coffee(String.valueOf(r.nextInt()), String.valueOf(r.nextInt())));

        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .post().uri("/coffee")
                .contentType(MediaType.APPLICATION_STREAM_JSON) // 2
                .body(eventFlux, Coffee.class) // 3
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
