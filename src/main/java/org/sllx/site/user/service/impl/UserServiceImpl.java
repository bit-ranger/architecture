package org.sllx.site.user.service.impl;


import org.sllx.site.core.service.CommonServiceSupport;
import org.sllx.site.user.dao.UserDao;
import org.sllx.site.user.entity.User;
import org.sllx.site.user.service.UserService;


public class UserServiceImpl extends CommonServiceSupport<User>  implements UserService {
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
        setDao(userDao);
        this.userDao = userDao;
	}
}
