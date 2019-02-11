package com.rainyalley.architecture.repository;

import com.rainyalley.architecture.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author bin.zhang
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

}
