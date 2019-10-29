package com.rainyalley.architecture.config;


import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration;
import com.rainyalley.architecture.graphql.SecurityContext;
import com.rainyalley.architecture.graphql.component.*;
import graphql.execution.ExecutorServiceExecutionStrategy;
import graphql.execution.instrumentation.ChainedInstrumentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.management.Query;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GraphqlConfig {

    @Bean(GraphQLWebAutoConfiguration.QUERY_EXECUTION_STRATEGY)
    ExecutorServiceExecutionStrategy queryExecutionStrategy(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                20,
                30L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Byte.MAX_VALUE),
                new CustomizableThreadFactory("owl-graphql-"));
        return new ExecutorServiceExecutionStrategy(executor, new CustomDataFetcherExceptionHandler());
    }

    @Bean
    CustomGraphqlErrorHandler customGraphqlErrorHandler(){
        return new CustomGraphqlErrorHandler();
    }

    @Bean
    ChainedInstrumentation securityInstrumentation(@Qualifier("securityContextHolderImpl") SecurityContext securityContext){
        return new ChainedInstrumentation(Arrays.asList(
                new SecurityInstrumentation(),
                new LogMdcInstrumentation(securityContext),
                new SavedRequestInstrumentation()));
    }


    @Bean
    Query graphqlQuery(){
        return new Query();
    }


    @Bean
    GraphqlTimeStampScalar graphqlTimeStampScalar(){
        return new GraphqlTimeStampScalar();
    }

}
