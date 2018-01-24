package com.rainyalley.architecture.boot.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author bin.zhang
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean(name = "batchTransactionManager")
    public ResourcelessTransactionManager resourcelessTransactionManager(){
        return new ResourcelessTransactionManager();
    }

    @Bean(name = "batchJobRepository")
    public MapJobRepositoryFactoryBean jobRepository(@Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager){
        MapJobRepositoryFactoryBean  jobRepository = new MapJobRepositoryFactoryBean();
        jobRepository.setTransactionManager(transactionManager);
        return jobRepository;
    }

    @Bean(name = "batchJobLauncher")
    public SimpleJobLauncher jobLauncher(@Qualifier("batchJobRepository") JobRepository jobRepository){
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }



}