package com.rainyalley.architecture.batch.demo;

import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User> {
    @Override
    public User process(User user) throws Exception {
        System.out.println("process: " + user.toString());
        return user;
    }
}
