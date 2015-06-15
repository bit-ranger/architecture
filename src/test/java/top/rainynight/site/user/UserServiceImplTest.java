package top.rainynight.site.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import top.rainynight.site.user.entity.User;
import top.rainynight.site.user.service.UserService;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback = true) //可选，默认就是这
public class UserServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    private UserService userService;

    @Test
    @Transactional
    @Rollback
    public void save(){
        User user = new User();
        user.setName("test");
        user.setPassword("test");
        userService.save(user);
    }
}
