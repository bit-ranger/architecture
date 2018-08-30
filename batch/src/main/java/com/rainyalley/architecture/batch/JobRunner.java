package com.rainyalley.architecture.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;

import javax.annotation.Resource;
import java.util.Date;

public class JobRunner {

    @Resource
    private JobOperator jobOperator;

    @Resource
    private JobExplorer jobExplorer;

    @Resource
    private JobRepository jobRepository;

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private JobRegistry jobRegistry;

    /**
     *
     * @param jobName
     * @param jobParameters
     * @return jobExecutionId
     * @throws Exception
     */
    public Long run(String jobName, JobParameters jobParameters) throws Exception{
        Job job = jobRegistry.getJob(jobName);
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if(lastJobExecution == null){
            return jobLauncher.run(job, jobParameters).getId();
        } else{
            if(lastJobExecution.isRunning()){
                return lastJobExecution.getId();
            } else if(lastJobExecution.getStatus().equals(BatchStatus.ABANDONED)){
                Date now = new Date();
                JobExecution jobExecution = jobExplorer.getJobExecution(lastJobExecution.getId());
                jobExecution.setStatus(BatchStatus.STOPPED);
                jobExecution.setEndTime(now);
                jobRepository.update(jobExecution);
                for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                    stepExecution.setStatus(BatchStatus.STOPPED);
                    stepExecution.setEndTime(now);
                    jobRepository.update(stepExecution);
                }
                return jobLauncher.run(job, jobParameters).getId();
            }
            return lastJobExecution.getJobId();
        }
    }

}
