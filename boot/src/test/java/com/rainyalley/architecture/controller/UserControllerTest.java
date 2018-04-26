package com.rainyalley.architecture.controller;

import com.rainyalley.architecture.BootApplication;
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

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootApplication.class})
@AutoConfigureMockMvc
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
        ResultActions ra = mvc.perform(MockMvcRequestBuilders.get("/user/X").accept(MediaType.APPLICATION_JSON));
//        ra.andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()));
//        ra.andExpect(header().string("Transfer-Encoding", not("chunked")));
//        MvcResult result = ra.andReturn();
        ra.andExpect(status().isNotFound());

    }

    @Test
    public void list() throws Exception {
    }

}