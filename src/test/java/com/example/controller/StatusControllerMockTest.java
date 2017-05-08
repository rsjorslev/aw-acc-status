package com.example.controller;

import com.example.service.AirWatchStatusService;
import com.example.service.LoginService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
//@ActiveProfiles("test")
@Ignore
public class StatusControllerMockTest {


    private MockRestServiceServer mockServer;

    @Autowired
    private AirWatchStatusService service;

    @Autowired
    private RestTemplate testRestTemplate;

    @Autowired
    private LoginService loginService;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(testRestTemplate);
    }

    @Test
    public void name() throws Exception {
        mockServer.expect(requestTo("/props")).andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        loginService.getCookies();
        service.getAccStatus();

        mockServer.verify();
    }
}