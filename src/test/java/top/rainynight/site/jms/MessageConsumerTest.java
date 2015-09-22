package top.rainynight.site.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.Destination;

/**
 * Created by sllx on 9/22/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-jms-test.xml"})
public class MessageConsumerTest {
    @Resource(name = "messageProducer")
    private MessageProducer messageProducer;

    @Resource(name = "queueDestination")
    private Destination queueDestination;

    @Resource(name = "topicDestination")
    private Destination topicDestination;

    @Test
    public void run(){
        for (int i=0; i<1; i++){
            messageProducer.sendMessage(queueDestination, "queue message : " + i);
//            messageProducer.sendMessage(topicDestination, "topic message : " + i);
        }

    }
}
