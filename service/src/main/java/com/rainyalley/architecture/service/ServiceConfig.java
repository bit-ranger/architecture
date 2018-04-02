package com.rainyalley.architecture.service;

import com.rainyalley.architecture.dao.DaoConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DaoConfig.class)
@EnableCaching
public class ServiceConfig {}
