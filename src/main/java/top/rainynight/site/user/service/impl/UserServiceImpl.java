package top.rainynight.site.user.service.impl;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.rainynight.core.ServiceBasicSupport;
import top.rainynight.site.user.dao.UserDao;
import top.rainynight.site.user.entity.User;
import top.rainynight.site.user.service.UserService;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl extends ServiceBasicSupport<User> implements UserService {
	private UserDao userDao;

    @Resource
	public void setUserDao(UserDao userDao) {
        setDao(userDao);
        this.userDao = userDao;
	}

    @Override
    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new User();
    }
}
