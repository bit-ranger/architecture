package com.rainyalley.architecture.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author bin.zhang
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.rainyalley.architecture.config",
        "com.rainyalley.architecture.impl"})
@EnableCaching
public class ServiceConfig {}
