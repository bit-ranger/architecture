package com.rainyalley.architecture.service.impl;


import com.rainyalley.architecture.dao.BaseMapper;
import com.rainyalley.architecture.dao.mapper.UserMapper;
import com.rainyalley.architecture.dao.entity.UserDo;
import com.rainyalley.architecture.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl extends ServiceBasicSupport<UserDo> implements UserService {

    @Resource
    private UserMapper userDao;


    @CachePut(key = "#p0.id")
    @Override
    public UserDo save(UserDo obj) {
        this.validate(obj);
        return super.save(obj);
    }

    @Override
    protected BaseMapper<UserDo> getDao() {
        return userDao;
    }

    @Cacheable(key = "#p0")
    @Override
    public UserDo get(int id) {
        UserDo p = new UserDo();
        p.setId(id);
        return super.get(p);
    }
}
