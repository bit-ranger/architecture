package com.rainyalley.architecture.service;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@MapperScan(basePackages = "com.rainyalley.architecture.service.*.dao", sqlSessionFactoryRef = "sqlSessionFactory")
public class ServiceConfiguration {

    static final String MAPPER_LOCATION = "classpath:mybatis/mapper/**.xml";

    @Value("${jdbc.master.url}")
    private String masterUrl;

    @Value("${jdbc.master.username}")
    private String masterUser;

    @Value("${jdbc.master.password}")
    private String masterPassword;

    @Value("${jdbc.master.driverClassName}")
    private String masterDriverClass;

    @Value("${jdbc.slave.url}")
    private String slaveUrl;

    @Value("${jdbc.slave.username}")
    private String slaveUser;

    @Value("${jdbc.slave.password}")
    private String slavePassword;

    @Value("${jdbc.slave.driverClassName}")
    private String slaveDriverClass;

    @Bean
    public DataSource dataSource() {
        DruidDataSource masterDataSource = new DruidDataSource();
        masterDataSource.setDriverClassName(masterDriverClass);
        masterDataSource.setUrl(masterUrl);
        masterDataSource.setUsername(masterUser);
        masterDataSource.setPassword(masterPassword);
        
//        DruidDataSource slaveDataSource = new DruidDataSource();
//        slaveDataSource.setDriverClassName(slaveDriverClass);
//        slaveDataSource.setUrl(slaveUrl);
//        slaveDataSource.setUsername(slaveUser);
//        slaveDataSource.setPassword(slavePassword);
//
//        AnnotationDataSourceRouter router = new AnnotationDataSourceRouter();
//        router.setDefaultTargetDataSource(masterDataSource);
//        router.setPointContextProvider(pointContextProvider);
//        Map<Object,Object> targetDataSources = new HashMap<Object, Object>();
//        targetDataSources.put("master", masterDataSource);
//        targetDataSources.put("slave", slaveDataSource);
//        router.setTargetDataSources(targetDataSources);
//        return router;
        return masterDataSource;
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
