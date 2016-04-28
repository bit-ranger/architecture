package com.rainyalley.architecture.message;


import com.rainyalley.architecture.message.producer.MessageProducer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-message-producer.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class MessageProducerTest {

    @Resource(name = "messageProducer")
    private MessageProducer producer;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void test() throws Exception {
        producer.send();
    }
}
