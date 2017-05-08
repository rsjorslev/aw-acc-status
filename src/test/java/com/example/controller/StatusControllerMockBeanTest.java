package com.example.controller;

import com.example.model.StatusResponse;
import com.example.service.AirWatchStatusService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@WebMvcTest(StatusController.class)
@Ignore
public class StatusControllerMockBeanTest {

    @MockBean
    private AirWatchStatusService service;

    @Autowired
    private MockMvc mvc;

    @Test
    public void name() throws Exception {

        given(this.service.getAccStatus())
                .willReturn(new StatusResponse(true, "success", Instant.now().getEpochSecond()));

        this.mvc.perform(get("/status/acc")).andExpect(status().isOk());
    }
}