package com.rainyalley.architecture.webservice.user;

import com.rainyalley.architecture.common.user.model.entity.User;
import com.rainyalley.architecture.common.user.service.UserService;
import com.rainyalley.architecture.core.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component("userWS")
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
