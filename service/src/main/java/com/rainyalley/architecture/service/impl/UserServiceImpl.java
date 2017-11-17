package com.rainyalley.architecture.service.impl;


import com.rainyalley.architecture.dao.Dao;
import com.rainyalley.architecture.dao.UserDao;
import com.rainyalley.architecture.model.entity.User;
import com.rainyalley.architecture.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl extends ServiceBasicSupport<User> implements UserService {

    @Resource
    private UserDao userDao;


    @CachePut(key = "#p0.id")
    @Override
    public User save(User obj) {
        this.validate(obj);
        return super.save(obj);
    }

    @Override
    protected Dao<User> getDao() {
        return userDao;
    }

    @Cacheable(key = "#p0")
    @Override
    public User get(int id) {
        User p = new User();
        p.setId(id);
        return super.get(p);
    }
}
