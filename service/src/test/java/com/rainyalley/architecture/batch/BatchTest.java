package com.rainyalley.architecture.batch;

import com.rainyalley.architecture.config.ServiceConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {ServiceConfiguration.class})
@SpringBootApplication
public class BatchTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    private JobOperator jobOperator;

    @Test
    public void test() throws Exception {
        jobOperator.start("demoJob", "token=" + System.currentTimeMillis());
    }

}