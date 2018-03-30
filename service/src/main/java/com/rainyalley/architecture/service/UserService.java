package com.rainyalley.architecture.service;


import com.rainyalley.architecture.dao.entity.UserDo;


public interface UserService extends Service<UserDo> {

    UserDo get(int id);
}
