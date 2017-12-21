package com.rainyalley.architecture.boot.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MockMvc mvc;

    @Resource
    private UserController userController;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }



    @Test
    public void get() throws Exception {
        ResultActions ra = mvc.perform(MockMvcRequestBuilders.get("/user/1").accept(MediaType.APPLICATION_JSON));
        ra.andExpect(status().isOk());
        ra.andExpect(header().string("Transfer-Encoding", not("chunked")));
        ra.andExpect(content().json("{\"id\":1,\"name\":\"中文名字\",\"password\":\"中文密码\"}"));
    }

    @Test
    public void list() throws Exception {
    }

}