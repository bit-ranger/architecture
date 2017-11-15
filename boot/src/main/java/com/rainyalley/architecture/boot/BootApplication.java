package com.rainyalley.architecture.boot;

import com.rainyalley.architecture.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(Arrays.asList(BootApplication.class, ServiceConfiguration.class).toArray(), args);
	}
}
