package com.rainyalley.architecture.batch.firstPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;

public class JdbcFirstPageRunner implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    private JobOperator jobOperator;

    @Resource
    private JobExplorer jobExplorer;

    @Resource
    private JobRepository jobRepository;

    @Override
    public void run() {
        try {
            Set<Long> exeIds = jobOperator.getRunningExecutions("firstPageCircleJob");
            logger.info(String.format("%s executions will restart", exeIds.size()));
            for (Long exeId : exeIds) {
                String param = jobOperator.getParameters(exeId);
                try {
                    Date now = new Date();
                    JobExecution jobExecution = jobExplorer.getJobExecution(exeId);
                    jobExecution.setStatus(BatchStatus.STOPPED);
                    jobExecution.setEndTime(now);
                    jobRepository.update(jobExecution);
                    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
                        stepExecution.setStatus(BatchStatus.STOPPED);
                        stepExecution.setEndTime(now);
                        jobRepository.update(stepExecution);
                    }

//                    boolean stop = jobOperator.stop(exeId);
//                    Assert.isTrue(stop);
                    //JobExecution jobExecution = jobOperator.abandon(exeId);

                } catch (Exception e) {
                    logger.error(String.format("restart failure, param:%s", param), e);
                }
            }

            long executionId = 0;
            if(exeIds.size() > 0){
                for (Long exeId : exeIds) {
                    executionId = jobOperator.restart(exeId);
                    logger.info(String.format("restart success, before exeId:%s, new exeId:%s", exeId, executionId));
                    break;
                }
            } else {
                executionId = jobOperator.start("firstPageCircleJob", "time=" + System.currentTimeMillis());
            }

            while (true){
                Set<Long> runningExecutions = jobOperator.getRunningExecutions("firstPageCircleJob");
                logger.info(String.format("%s executions running, will stop", runningExecutions.size()));
                if(runningExecutions.size() == 1){
                    break;
                }

                Thread.sleep(1000);
            }

            jobOperator.stop(executionId);

            while (true){
                Set<Long> runningExecutions = jobOperator.getRunningExecutions("firstPageCircleJob");
                logger.info(String.format("%s executions running", runningExecutions.size()));
                if(runningExecutions.size() == 0){
                    break;
                }

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
