package com.rainyalley.architecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author bin.zhang
 */
@SpringBootApplication(
	scanBasePackages = {
		"com.rainyalley.architecture.graphql",
		"com.rainyalley.architecture.config",
		"com.rainyalley.architecture.controller",
		"com.rainyalley.architecture.impl"})
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}

