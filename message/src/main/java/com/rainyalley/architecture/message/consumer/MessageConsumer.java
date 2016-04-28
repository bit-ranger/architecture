package com.rainyalley.architecture.message.consumer;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by sllx on 9/22/15.
 */

@Component("messageConsumer")
public class MessageConsumer {

    /**
     * @param message
     * @throws javax.jms.JMSException
     */
    public String onMessage(String message) throws JMSException {
        System.out.println("Received : " + message);
        return "reply -> " + message;
    }
}
