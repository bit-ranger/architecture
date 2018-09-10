package com.rainyalley.architecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author bin.zhang
 */
@EnableEurekaClient
@SpringBootApplication(
	scanBasePackages = {
		"com.rainyalley.architecture.config",
		"com.rainyalley.architecture.controller",
		"com.rainyalley.architecture.aop",
		"com.rainyalley.architecture.impl"},
	exclude = {DataSourceAutoConfiguration.class})
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}

