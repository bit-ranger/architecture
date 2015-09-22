package top.rainynight.site.user.ws;

import org.springframework.stereotype.Service;
import top.rainynight.core.util.Page;
import top.rainynight.site.user.entity.User;
import top.rainynight.site.user.service.UserService;

import java.util.List;


@Service("userWS")
public class UserWSImpl implements UserWS {

    @javax.annotation.Resource(name = "userService")
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
