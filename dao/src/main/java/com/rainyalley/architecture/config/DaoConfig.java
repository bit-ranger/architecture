package com.rainyalley.architecture.config;

import com.rainyalley.architecture.SimpleStreamProvider;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Configuration
public class DaoConfig {

    @Bean(destroyMethod = "close")
    SimpleStreamProvider simpleStreamProvider(EntityManagerFactory entityManagerFactory,
                                              EntityManager entityManager){
        JinqJPAStreamProvider streams = new JinqJPAStreamProvider(entityManagerFactory);
        return new SimpleStreamProvider(streams, entityManager);
    }

}
