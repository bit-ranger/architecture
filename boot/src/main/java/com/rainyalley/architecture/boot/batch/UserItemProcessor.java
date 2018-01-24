package com.rainyalley.architecture.boot.batch;

import com.rainyalley.architecture.model.entity.User;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User> {
    @Override
    public User process(User user) throws Exception {
        System.out.println("process: " + user.toString());
        return user;
    }
}
