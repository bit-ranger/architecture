package com.rainyalley.architecture.impl;


import com.rainyalley.architecture.service.UserService;
import com.rainyalley.architecture.BaseMapper;
import com.rainyalley.architecture.entity.UserDo;
import com.rainyalley.architecture.mapper.UserMapper;
import com.rainyalley.architecture.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl extends ServiceBasicSupport<User,UserDo> implements UserService {

    @Resource
    private UserMapper userMapper;


    @CachePut(key = "#result.id",condition = "#result != null")
    @Override
    @Transactional
    public User save(User obj) {
        return super.save(obj);
    }

    @Override
    protected BaseMapper<UserDo> getDao() {
        return userMapper;
    }

    @Override
    protected UserDo toDo(User b) {
        UserDo d  = new UserDo();
        d.setId(b.getId());
        d.setName(b.getName());
        d.setPassword(b.getPassword());
        return d;
    }

    @Override
    protected User toBo(UserDo d) {
        User user  = new User();
        user.setId(d.getId());
        user.setName(d.getName());
        user.setPassword(d.getPassword());
        return user;
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#id")
    @Override
    public User get(String id) {
        return super.get(id);
    }
}
