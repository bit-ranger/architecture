package com.rainyalley.architecture.impl;


import com.rainyalley.architecture.Page;
import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.po.UserAdd;
import com.rainyalley.architecture.repository.UserRepository;
import com.rainyalley.architecture.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author bin.zhang
 */
@CacheConfig(cacheNames = "user")
@Slf4j
@Setter
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;


    @Cacheable(key = "#id")
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    @Override
    public Mono<User> get(Mono<Long> id) {
        return userRepository.findById(id).map(this::map);
    }


    @Override
    public Flux<User> list(Mono<Page> page) {
        return page
                .flatMapMany(p -> userRepository.findAll()
                .map(this::map));
    }


    @Override
    public Mono<User> add(Mono<UserAdd> userAdd) {
        return userAdd
                .flatMap(a -> userRepository.save(map(a)))
                .map(this::map);
    }

    @Override
    public Mono<User> remove(Mono<Long> id) {
        return id
                .flatMap(i -> userRepository.findById(id))
                .doOnNext(e -> userRepository.deleteById(e.getId()).subscribe())
                .map(this::map);
    }

    private User map(com.rainyalley.architecture.entity.User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }


    private com.rainyalley.architecture.entity.User map(UserAdd userAdd) {
        com.rainyalley.architecture.entity.User e = new com.rainyalley.architecture.entity.User();
        e.setId(System.currentTimeMillis());
        e.setName(userAdd.getName());
        e.setPassword(userAdd.getPassword());
        return e;
    }
}
