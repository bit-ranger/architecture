package org.sllx.user.service.impl;


import org.sllx.core.service.CommonServiceSupport;
import org.sllx.user.dao.UserDao;
import org.sllx.user.entity.User;
import org.sllx.user.service.UserService;


public class UserServiceImpl extends CommonServiceSupport<User> implements UserService {
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
        setDao(userDao);
        this.userDao = userDao;
	}

}
