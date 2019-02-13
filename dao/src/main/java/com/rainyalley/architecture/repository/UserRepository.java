package com.rainyalley.architecture.repository;

import com.rainyalley.architecture.entity.User;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author bin.zhang
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Tailable
    Flux<User> findBy();
}
