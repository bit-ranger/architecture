package com.rainyalley.architecture.service;


import com.rainyalley.architecture.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> get(Long id);
}
