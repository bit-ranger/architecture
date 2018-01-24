package com.rainyalley.architecture.boot.controller;


import com.rainyalley.architecture.boot.batch.UserAggregator;
import com.rainyalley.architecture.boot.batch.UserItemProcessor;
import com.rainyalley.architecture.boot.batch.UserLineMapper;
import com.rainyalley.architecture.model.entity.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Resource
    private JobRepository jobRepository;

    @Resource
    private JobLauncher jobLauncher;

    @Resource(name = "transactionManager")
    private PlatformTransactionManager transactionManager;

    @Bean
    @Primary
    public JobBuilderFactory jobBuilders(JobRepository jobRepository) throws Exception {
        return new JobBuilderFactory(jobRepository);
    }


    @RequestMapping("/user")
    public ResponseEntity<Object> user(){
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("src/main/resources/architecture_user.csv"));
        reader.setLineMapper(new UserLineMapper());

        UserItemProcessor processor = new UserItemProcessor();

        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("src/main/resources/architecture_user_w.csv"));
        writer.setLineAggregator(new UserAggregator());

        StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, transactionManager);
        Step step = stepBuilderFactory.get("step").<User,User>chunk(1).reader(reader).processor(processor).writer(writer).build();

        JobBuilderFactory jbf = new JobBuilderFactory(jobRepository);
        Job job = jbf.get("job").start(step).build();

        try {
            jobLauncher.run(job, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }
}
