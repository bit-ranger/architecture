package com.rainyalley.architecture.common.user.service.impl;


import com.rainyalley.architecture.common.ServiceBasicSupport;
import com.rainyalley.architecture.common.user.dao.UserDao;
import com.rainyalley.architecture.common.user.model.entity.User;
import com.rainyalley.architecture.common.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl extends ServiceBasicSupport<User> implements UserService {
    private UserDao userDao;

    @Resource
    public void setUserDao(UserDao userDao) {
        this.setDao(userDao);
        this.userDao = userDao;
    }


    @Override
    public int save(User obj) {
        this.validate(obj);
//        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
//        passwordEncoder.setEncodeHashAsBase64(true);
//        String encodedPasswrod = passwordEncoder.encodePassword(obj.getPassword(),obj.getName());
//        obj.setPassword(encodedPasswrod);
        return super.save(obj);
    }
}
