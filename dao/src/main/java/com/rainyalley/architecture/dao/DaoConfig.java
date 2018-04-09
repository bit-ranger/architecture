package com.rainyalley.architecture.dao;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan(basePackages="com.rainyalley.architecture.dao.mapper")
public class DaoConfig {


    /**
     * DBCP DataSource configuration.
     */
    @ConditionalOnClass(DruidDataSource.class)
    @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
    private static class Druid  {

        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.druid")
        public DataSource dataSource(DataSourceProperties properties) {
            return properties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
        }

    }

}
