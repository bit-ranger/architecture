package com.rainyalley.architecture.impl;


import com.rainyalley.architecture.Page;
import com.rainyalley.architecture.StreamProvider;
import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.po.UserAdd;
import com.rainyalley.architecture.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jinq.jpa.JPAJinqStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author bin.zhang
 */
@Slf4j
@Setter
@Service
public class UserServiceImpl implements UserService {

    @Resource(name = "simpleStreamProvider")
    private StreamProvider streamProvider;

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    @Override
    public Mono<User> get(Mono<Long> id) {
        return id
                .flatMap(i -> Mono.justOrEmpty(get(i)))
                .onErrorResume(e -> {
                    log.error("get failure", e);
                    return Mono.empty();
                });
    }

    private Optional<User> get(Long id){
        JPAJinqStream<com.rainyalley.architecture.entity.User> stream =
                streamProvider.stream(com.rainyalley.architecture.entity.User.class);
        return stream.where(c -> c.getId().equals(id)).map(this::map).findFirst();
    }

    @Override
    public Flux<User> list(Mono<Page> page) {
        return page.map(this::list).flatMapMany(Flux::fromStream);
    }

    private Stream<User> list(Page page){
        JPAJinqStream<com.rainyalley.architecture.entity.User> stream =
                streamProvider.stream(com.rainyalley.architecture.entity.User.class);
        return stream
                .skip((page.getPageNum() - 1) * page.getPageSize())
                .limit(page.getPageSize())
                .map(this::map);
    }

    @Override
    public Mono<User> add(Mono<UserAdd> userAdd) {
        return userAdd.flatMap(a -> {
            com.rainyalley.architecture.entity.User e = map(a);
            streamProvider.persist(e);
            return Mono.just(map(e));
        });
    }

    private User map(com.rainyalley.architecture.entity.User user){
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }

    private com.rainyalley.architecture.entity.User map(UserAdd userAdd){
        com.rainyalley.architecture.entity.User e = new com.rainyalley.architecture.entity.User();
        e.setName(userAdd.getName());
        e.setPassword(userAdd.getPassword());
        return e;
    }
}
