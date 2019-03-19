package com.mermaid.framework.controller;

import com.mermaid.framework.ApplicationTest;
import com.mermaid.framework.core.controller.CoreController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.locks.ReentrantLock;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/2 23:44
 */
@Slf4j
public class CoreControllerTest extends ApplicationTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CoreController()).build();
    }

    @Test
    public void getCoreConfig() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/core/get").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        log.info("response code={}",response.getStatus());
    }

    @Test
    public void getCoreConfigByBalence() throws Exception {
        ReentrantLock lock = new ReentrantLock();
    }

}