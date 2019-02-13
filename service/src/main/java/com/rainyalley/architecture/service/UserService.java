package com.rainyalley.architecture.service;


import com.rainyalley.architecture.Page;
import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.po.UserAdd;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> get(Mono<Long> id);

    Flux<User> list(Mono<Page> page);

    Flux<User> tail(Mono<Page> page);

    Mono<User> add(Mono<UserAdd> userAdd);

    Flux<User> add(Flux<UserAdd> userAdd);

    Mono<User> remove(Mono<Long> id);
}
