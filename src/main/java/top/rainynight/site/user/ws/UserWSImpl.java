package top.rainynight.site.user.ws;

import org.springframework.stereotype.Component;
import top.rainynight.core.util.Page;
import top.rainynight.site.user.entity.User;
import top.rainynight.site.user.service.UserService;

import javax.jws.WebService;
import java.util.List;


@Component("userWS")
@WebService
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
        user.setUserid(id);
        return userService.get(user);
    }
}
