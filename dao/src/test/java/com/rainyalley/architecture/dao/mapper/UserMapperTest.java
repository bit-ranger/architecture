package com.rainyalley.architecture.dao.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
@Transactional
public class UserMapperTest {

    @Test
    @Rollback
    public void test(){

    }
}