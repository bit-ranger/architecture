package com.rainyalley.architecture.service.user.service.impl;


import com.rainyalley.architecture.service.ServiceBasicSupport;
import com.rainyalley.architecture.service.user.dao.UserDao;
import com.rainyalley.architecture.service.user.model.entity.User;
import com.rainyalley.architecture.service.user.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl extends ServiceBasicSupport<User> implements UserService {
    private UserDao userDao;

    @Resource
    public void setUserDao(UserDao userDao) {
        this.setDao(userDao);
        this.userDao = userDao;
    }


    @CachePut(key = "#p0.id")
    @Override
    public int save(User obj) {
        this.validate(obj);
//        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
//        passwordEncoder.setEncodeHashAsBase64(true);
//        String encodedPasswrod = passwordEncoder.encodePassword(obj.getPassword(),obj.getName());
//        obj.setPassword(encodedPasswrod);
        return super.save(obj);
    }

    @Cacheable(key = "#p0")
    @Override
    public User get(int id) {
        User p = new User();
        p.setId(id);
        return super.get(p);
    }
}
