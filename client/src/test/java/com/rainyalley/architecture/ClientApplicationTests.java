package com.rainyalley.architecture;

import com.rainyalley.architecture.client.UserClient;
import com.rainyalley.architecture.config.ClientConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ClientConfig.class})
public class ClientApplicationTests {

    @Autowired
    private UserClient userClient;

    @Test
    public void contextLoads() {
        String actuator =   userClient.actuator();
        System.out.println(actuator);
    }

}
