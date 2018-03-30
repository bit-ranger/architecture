package com.rainyalley.architecture.batch;

import com.rainyalley.architecture.service.config.ServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {ServiceConfig.class})
@SpringBootApplication
public class BatchTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    private JobOperator jobOperator;

    @Test
    public void test() throws Exception {
        Set<Long> exeIds = jobOperator.getRunningExecutions("demoJob");
        logger.info(String.format("%s executions will restart", exeIds.size()));
        for (Long exeId : exeIds) {
            String param = jobOperator.getParameters(exeId);
            try {
                boolean stop = jobOperator.stop(exeId);
                Assert.isTrue(stop);
                JobExecution jobExecution = jobOperator.abandon(exeId);
                Long newExeId = jobOperator.restart(jobExecution.getId());
                logger.info(String.format("restart success, before exeId:%s, new exeId:%s", exeId, newExeId));
            } catch (Exception e) {
                logger.error(String.format("restart failure, param:%s", param), e);
            }
        }

        jobOperator.start("demoJob", "time=" + System.currentTimeMillis());

        while (true){
            Set<Long> runningExecutions = jobOperator.getRunningExecutions("demoJob");
            if(runningExecutions.size() == 0){
                break;
            }
            logger.info(String.format("%s executions running", runningExecutions.size()));
            Thread.sleep(5000);
        }
    }

}