package com.example.controller;

import com.example.exception.HostNotFoundException;
import com.example.model.StatusResponse;
import com.example.service.LoginService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Only kept as a reference to show the differences vs {@link StatusControllerTest}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost = "example.com")
@AutoConfigureMockMvc
//@ActiveProfiles("test")
@Ignore
public class StatusControllerTestOLD {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate testRestTemplate;

    @MockBean
    private LoginService loginService;

    @Before
    public void setUp() {
        StatusResponse responseFailure = new StatusResponse();
        responseFailure.setSuccess(false);
        responseFailure.setTimestamp(Instant.now().getEpochSecond());
        responseFailure.setMessage("Failed");

        when(testRestTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class)
        )).thenReturn(new ResponseEntity<>(responseFailure, HttpStatus.BAD_REQUEST));

    }

    @Test
    public void adStatusCheckShouldReturnSuccess() throws Exception {
        StatusResponse responseSuccess = new StatusResponse();
        responseSuccess.setSuccess(true);
        responseSuccess.setTimestamp(Instant.now().getEpochSecond());
        responseSuccess.setMessage("Succeeded");

        when(testRestTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class)
        )).thenReturn(new ResponseEntity<>(responseSuccess, HttpStatus.OK));

        mockMvc.perform(get("/status/acc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IsSuccess", is(true)))
                .andDo(document("status-acc-success", preprocessResponse(prettyPrint())));
    }

    @Test
    public void accStatusCheckShouldReturnSuccess() throws Exception {
        StatusResponse responseSuccess = new StatusResponse();
        responseSuccess.setSuccess(true);
        responseSuccess.setTimestamp(Instant.now().getEpochSecond());
        responseSuccess.setMessage("Succeeded");

        when(testRestTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class)
        )).thenReturn(new ResponseEntity<>(responseSuccess, HttpStatus.OK));

        mockMvc.perform(get("/status/acc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IsSuccess", is(true)))
                .andDo(document("status-ad-success", preprocessResponse(prettyPrint())));
    }

    @Test
    public void adStatusCheckShouldReturnFailedState() throws Exception {
        mockMvc.perform(get("/status/acc").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IsSuccess", is(false)))
                .andDo(document("status-acc-failure", preprocessResponse(prettyPrint())));
    }

    @Test
    public void accStatusCheckShouldReturnFailedState() throws Exception {
        mockMvc.perform(get("/status/ad").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.IsSuccess", is(false)))
                .andExpect(status().isOk())
                .andDo(document("status-ad-failure", preprocessResponse(prettyPrint())));
    }

    @Test
    public void invalidHostShouldThrowHostNotFoundException() throws Exception {
        when(testRestTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class)
        )).thenThrow(HostNotFoundException.class);

        mockMvc.perform(get("/status/acc").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                //.andExpect(jsonPath("$", is("asdf")))
                .andDo(document("host-not-found", preprocessResponse(prettyPrint())));
    }

    @TestConfiguration
    static class CustomizationConfiguration {

        @Bean
        public RestDocumentationResultHandler restDocumentation() {
            return MockMvcRestDocumentation.document("{ClassName}/{methodName}", preprocessResponse(prettyPrint()));
        }

    }

}