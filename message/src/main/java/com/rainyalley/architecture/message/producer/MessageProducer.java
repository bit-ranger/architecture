package com.rainyalley.architecture.message.producer;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;


@Component("messageProducer")
public class MessageProducer{
    private JmsTemplate jmsTemplate;

    @Resource(name = "queueDestination")
    private Destination queueDestination;

    @Resource(name = "topicDestination")
    private Destination topicDestination;


    public void send(){
        sendMessage(queueDestination, "this is a queue message!");
        sendMessage(queueDestination, "this is a topic message!");
    }

    public void sendMessage(Destination destination, final String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @Resource
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

}
