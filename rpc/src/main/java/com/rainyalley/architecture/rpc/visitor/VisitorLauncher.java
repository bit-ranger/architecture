package com.rainyalley.architecture.rpc.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainyalley.architecture.common.user.model.entity.User;
import com.rainyalley.architecture.common.user.service.UserService;
import com.rainyalley.architecture.core.Page;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import java.util.List;

public class VisitorLauncher {

    public static void main(String[] args) throws Exception {
        Log4jConfigurer.initLogging("classpath:log4j.xml");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-rpc-visitor.xml");
        context.start();

        UserService userService = (UserService)context.getBean("remoteUserService"); // 获取远程服务代理
        List<User> users = userService.get(new User(), new Page());

        System.out.println(new ObjectMapper().writeValueAsString(users));
    }
}
