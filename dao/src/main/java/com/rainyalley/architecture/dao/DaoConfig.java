package com.rainyalley.architecture.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.rainyalley.architecture.dao.util.RoutingDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan(basePackages="com.rainyalley.architecture.dao.mapper")
public class DaoConfig {


    @Primary
    @Bean("primaryDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties primaryDataSourceProperties(){
        return new DataSourceProperties();
    }


    @ConditionalOnClass(DruidDataSource.class)
    @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
    private static class PrimaryDruid {

        @Bean("primaryDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.druid")
        public DataSource dataSource(DataSourceProperties properties) {
            return properties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
        }


    }

    @ConditionalOnClass(DruidDataSource.class)
    @ConditionalOnProperty(name = "extend.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
    private static class SecondaryDruid {

        @Bean("secondaryDataSourceProperties")
        @ConfigurationProperties(prefix = "extend.datasource")
        public DataSourceProperties secondaryDataSourceProperties(){
            return new DataSourceProperties();
        }

        @Bean("secondaryDataSource")
        @ConfigurationProperties(prefix = "extend.datasource.druid")
        public DataSource dataSource(@Qualifier("secondaryDataSourceProperties") DataSourceProperties properties) {
            return properties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
        }
    }



    @ConditionalOnClass({ RoutingDataSource.class})
    private static class Routing{

        @Primary
        @Bean("dataSource")
        public DataSource routingDataSource(@Qualifier("primaryDataSource") DataSource writeDataSource, @Qualifier("secondaryDataSource") DataSource readDataSource){
            RoutingDataSource rds = new RoutingDataSource();
            Map<Object, Object> map = new HashMap<>(4);
            map.put("primary", writeDataSource);
            map.put("secondary",  readDataSource);
            rds.setTargetDataSources(map);
            rds.setDefaultTargetDataSource(writeDataSource);
            return rds;
        }
    }

}
