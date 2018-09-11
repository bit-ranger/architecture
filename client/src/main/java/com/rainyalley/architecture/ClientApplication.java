package com.rainyalley.architecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author bin.zhang
 */
@SpringBootApplication(
	scanBasePackages = {
		"com.rainyalley.architecture.config",
		"com.rainyalley.architecture.client",
		"com.rainyalley.architecture.controller"}
	)
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
}

