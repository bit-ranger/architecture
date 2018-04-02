package com.rainyalley.architecture.boot;

import com.rainyalley.architecture.service.ServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author bin.zhang
 */
@SpringBootApplication(scanBasePackages = {
		"com.rainyalley.architecture.boot.controller",
		"com.rainyalley.architecture.boot.config"})
@Import(ServiceConfig.class)
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}

