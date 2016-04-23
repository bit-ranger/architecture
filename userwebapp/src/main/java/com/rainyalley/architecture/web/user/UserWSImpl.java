package com.rainyalley.architecture.web.user;

import com.rainyalley.architecture.core.Page;
import com.rainyalley.common.user.model.entity.User;
import com.rainyalley.common.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("userWS")
public class UserWSImpl implements UserWS {

    @Resource(name = "userService")
    private UserService userService;

    @Override
    public List<User> browse() {
        return userService.get(new User(), new Page());
    }

    @Override
    public User lookOver(int id) {
        User user = new User();
        user.setId(id);
        return userService.get(user);
    }
}
