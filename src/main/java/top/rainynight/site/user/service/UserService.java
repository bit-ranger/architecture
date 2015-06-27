package top.rainynight.site.user.service;


import top.rainynight.core.Service;
import top.rainynight.site.user.entity.User;


public interface UserService extends Service<User> {

    User currentUser();
}
