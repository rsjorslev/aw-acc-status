package com.example.service;

import com.example.AwProperties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * This is kept as a reference to show how this differes from the "Spring Way" in {@link LoginServiceTest}
 */
@Ignore
public class LoginServiceExampleT {

    RestTemplate template;
    LoginService service;
    MockRestServiceServer server;
    AwProperties properties;

    String LOGIN_TOKEN = "hCnZmPGck-lVEbS5ZD-XNuUT1cU_XGKJjg26BvdLaCtXjgItdi4TH_TC6uFxP3uoMPKGbclekCV_K8CY11hTa2g5MP1n4L8o7m7PYYiOLy81";
    String INPUT = "input name=\"__RequestVerificationToken\" type=\"hidden\" value=\"hCnZmPGck-lVEbS5ZD-XNuUT1cU_XGKJjg26BvdLaCtXjgItdi4TH_TC6uFxP3uoMPKGbclekCV_K8CY11hTa2g5MP1n4L8o7m7PYYiOLy81\" />";

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        service = new LoginService(restTemplate, properties);
    }

    @Test
    public void getLoginTokenTest() throws Exception {

        server.expect(manyTimes(), requestTo("/AirWatch/Login")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(INPUT, MediaType.APPLICATION_XHTML_XML));

        assertThat(service.getLoginToken(), equalTo(LOGIN_TOKEN));

    }
}
