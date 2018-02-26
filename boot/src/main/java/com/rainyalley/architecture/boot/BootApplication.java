package com.rainyalley.architecture.boot;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author bin.zhang
 */
@EnableBatchProcessing
@SpringBootApplication(scanBasePackages = {"com.rainyalley.architecture"})
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}

