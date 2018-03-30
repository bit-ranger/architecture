package com.rainyalley.architecture.service;


import com.rainyalley.architecture.service.model.User;


public interface UserService extends Service<User> {

    User get(int id);
}
