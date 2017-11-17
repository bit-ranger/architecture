package com.rainyalley.architecture.service;


import com.rainyalley.architecture.model.entity.User;


public interface UserService extends Service<User> {

    User get(int id);
}
