package top.rainynight.site.user.service.impl;


import org.springframework.stereotype.Service;
import top.rainynight.core.ServiceBasicSupport;
import top.rainynight.site.user.dao.UserDao;
import top.rainynight.site.user.entity.User;
import top.rainynight.site.user.service.UserService;

@Service("userService")
public class UserServiceImpl extends ServiceBasicSupport<User> implements UserService {
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
        setDao(userDao);
        this.userDao = userDao;
	}
}
