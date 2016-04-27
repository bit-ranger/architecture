package com.rainyalley.architecture.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

public class ProviderLauncher {

    public static void main(String[] args) throws Exception {
        Log4jConfigurer.initLogging("classpath:log4j.xml");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-provider.xml");
        context.start();

        System.in.read(); // 按任意键退出
    }
}
