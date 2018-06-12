package com.rainyalley.architecture.filter.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainyalley.architecture.BootApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootApplication.class})
@AutoConfigureMockMvc
public class XssFilterTest implements ApplicationContextAware {

    private MockMvc mvc;

    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext)applicationContext).addFilter(new XssFilter()).build();
    }


    @Test
    public void init() throws Exception {
    }

    @Test
    public void doFilter() throws Exception {

        Map<String,String> param = new HashMap<>();
        param.put("returnurl", "https%3A%2F%2Flab.chinapnr.com%2Fmusermag%2F%22%3E%3Cscript%3Ealert%2851%29%3C%2Fscript%3E<script>");
        String body = new ObjectMapper().writeValueAsString(param);
        ResultActions ra = mvc.perform(MockMvcRequestBuilders.post("/user").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(body));
        ra.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void doFilterMultipart() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("file", "other-file-name.data", "text/plain", "some other type".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("file", "", "application/json", "{\"json\": \"someValue\"}".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/user")
                .file(firstFile)
                .file(secondFile)
                .file(jsonFile)
                .param("user", "{\"name\": \"someValue>\"}")
                .param("user", "{\"age\": \"someValue\"}")
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void destroy() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}