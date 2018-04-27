package com.rainyalley.architecture;

import com.didispace.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author bin.zhang
 */
@SpringBootApplication(
	scanBasePackages = {
		"com.rainyalley.architecture.config",
		"com.rainyalley.architecture.controller",
		"com.rainyalley.architecture.aop",
		"com.rainyalley.architecture.impl"},
	exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2Doc
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}

