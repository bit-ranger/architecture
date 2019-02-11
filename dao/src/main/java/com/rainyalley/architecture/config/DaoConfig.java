package com.rainyalley.architecture.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.rainyalley.architecture.repository")
@Configuration
public class DaoConfig {


}
