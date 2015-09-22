package top.rainynight.site.jms;

import org.springframework.stereotype.Service;
import top.rainynight.site.user.entity.User;
import top.rainynight.site.user.service.UserService;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by sllx on 9/22/15.
 */
@Service("messageConsumer")
public class MessageConsumer {

    private int count;

    @Resource(name = "userService")
    private UserService userService;

    /**
     * 测试JTA事务
     * 每次接收信息都应当打印并入库
     * 预计此函数中会打印并入库一次
     * 然后回滚
     * 然后在打印并入库
     * 总计打印2次消息，1次异常，库中增加1条记录
     * @param message
     * @throws JMSException
     */
    public void onMessage(String message) throws JMSException {
        System.out.println("Received : " + message);
        User user = new User();
        user.setName("success");
        user.setPassword("123456");
        userService.save(user);
        if(count == 0){
            count ++;
            throw new RuntimeException();
        }

        //adapter 自动 reply
        //return "reply -> " + message;
    }
}
