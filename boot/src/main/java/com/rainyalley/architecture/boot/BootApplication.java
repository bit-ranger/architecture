package com.rainyalley.architecture.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ImportResource(value =
//		{"classpath:spring-boot-mvc.xml",
//		"classpath:spring-boot-security.xml"})
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}
