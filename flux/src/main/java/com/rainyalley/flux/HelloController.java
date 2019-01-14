package com.rainyalley.flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

@RestController
public class HelloController {

    @Autowired
    CoffeeRepository coffeeLoader;

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world ~");
    }

//    @GetMapping(value = "/coffee", produces = APPLICATION_JSON_VALUE)
//    public Flux<Coffee> coffee(){
//        return coffeeLoader.load();
//    }

    @GetMapping(value = "/coffee", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<Coffee> coffeeStream(){
        return coffeeLoader.load().delayElements(Duration.ofSeconds(1));
    }

    @PostMapping(path = "/coffee", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE) // 1
    public Flux<Boolean> postCoffee(@RequestBody Flux<Coffee> coffeeFlux) {
        return coffeeLoader.insert(coffeeFlux);
    }
}
