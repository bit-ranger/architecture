package top.rainynight.site.jms;

import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * Created by sllx on 9/22/15.
 */
@Service("messageConsumer")
public class MessageConsumer implements MessageListener,ExceptionListener {
    @Override
    public void onException(JMSException exception) {

    }

    @Override
    public void onMessage(Message message) {
        try {

            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String msg = txtMsg.getText();
                if (msg.length() > 50) {
                    msg = msg.substring(0, 50) + "...";
                }

                System.out.println("Received: " + msg);
            } else {
                System.out.println("Received: " + message);
            }

        } catch (JMSException e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

}
