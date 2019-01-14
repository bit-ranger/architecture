package com.rainyalley.flux;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class CoffeeRepository {

    private static final String KEY = "CoffeeRepository:";

    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, Coffee> coffeeOps;

    public CoffeeRepository(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Coffee> coffeeOps) {
        this.factory = factory;
        this.coffeeOps = coffeeOps;
    }

    public Flux<Coffee> load() {
        return coffeeOps.keys(KEY + "*")
                .flatMap(k -> coffeeOps.opsForValue().get(k));
    }

    public Flux<Boolean> insert(Flux<Coffee> coffeeFlux){
        return coffeeFlux.flatMap(p -> coffeeOps.opsForValue().set(KEY + p.getId(), p));
    }
}
