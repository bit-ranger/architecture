package com.rainyalley.architecture.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author bin.zhang
 */
@EnableCaching(proxyTargetClass = true)
@Configuration
public class ServiceConfig {}
