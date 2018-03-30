package com.rainyalley.architecture.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author bin.zhang
 */
@SpringBootApplication(scanBasePackages = {"com.rainyalley.architecture.dao", "com.rainyalley.architecture.service", "com.rainyalley.architecture.boot"})
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}

