package com.rainyalley.architecture.service.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@EnableCaching
@SpringBootApplication(scanBasePackages = "com.rainyalley.architecture.service")
public class ServiceConfig {

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


}
