package com.rainyalley.architecture.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author bin.zhang
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.rainyalley.architecture.config",
        "com.rainyalley.architecture.aop",
        "com.rainyalley.architecture.impl"},
    exclude = {
        DataSourceAutoConfiguration.class})
@EnableCaching
public class ServiceConfig {}
