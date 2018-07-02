package com.mermaid.framework.controller;

import com.mermaid.framework.ApplicationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/2 23:44
 */

public class CoreControllerTest extends ApplicationTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CoreController()).build();
    }

    @Test
    public void getCoreConfig() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/getBalence").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getCoreConfigByBalence() throws Exception {
    }

}