package com.rainyalley.architecture.service.impl;


import com.rainyalley.architecture.dao.BaseMapper;
import com.rainyalley.architecture.dao.entity.UserDo;
import com.rainyalley.architecture.dao.mapper.UserMapper;
import com.rainyalley.architecture.service.UserService;
import com.rainyalley.architecture.service.model.User;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl extends ServiceBasicSupport<User,UserDo> implements UserService {

    @Resource
    private UserMapper userMapper;


    @CachePut(key = "#p0.id")
    @Override
    public User save(User obj) {
        this.validate(obj);
        return super.save(obj);
    }

    @Override
    protected BaseMapper<UserDo> getDao() {
        return userMapper;
    }

    @Override
    protected UserDo toDo(User b) {
        UserDo userDo  = new UserDo();
        try {
            PropertyUtils.copyProperties(userDo, b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userDo;
    }

    @Override
    protected User toBo(UserDo d) {
        User user  = new User();
        try {
            PropertyUtils.copyProperties(user, d);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Cacheable(key = "#p0")
    @Override
    public User get(int id) {
        User p = new User();
        p.setId(id);
        return super.get(p);
    }
}
