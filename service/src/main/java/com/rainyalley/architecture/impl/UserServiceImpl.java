package com.rainyalley.architecture.impl;


import com.rainyalley.architecture.StreamProvider;
import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jinq.jpa.JPAJinqStream;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author bin.zhang
 */
@Slf4j
@Setter
@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl implements UserService {

    @Resource(name = "simpleStreamProvider")
    private StreamProvider streamProvider;

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
//    @Cacheable(key = "#id")
    @Override
    public Optional<User> get(Long id) {

        try {
            JPAJinqStream<com.rainyalley.architecture.entity.User> stream =
                    streamProvider.stream(com.rainyalley.architecture.entity.User.class);
            return stream.where(c -> c.getId().equals(id)).map(this::map).findFirst();
        } catch (Exception e) {
            log.error("get failure", e);
            return Optional.empty();
        }
    }

    private User map(com.rainyalley.architecture.entity.User user){
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }
}
