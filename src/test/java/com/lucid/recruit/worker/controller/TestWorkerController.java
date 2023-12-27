package com.lucid.recruit.worker.controller;

import com.lucid.base.test.BaseTest1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestWorkerController extends BaseTest1 {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testCreateWorker() throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/api/raves/v1/worker")
                .contentType(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isOk());
    }

}
