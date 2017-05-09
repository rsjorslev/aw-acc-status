package com.example.controller;

import com.example.exception.AuthenticationException;
import com.example.exception.HostNotFoundException;
import com.example.model.StatusResponse;
import com.example.service.AirWatchStatusService;
import com.example.service.ServiceStateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link StatusController}
 */
@RunWith(SpringRunner.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost = "example.com")
@AutoConfigureMockMvc
@WebMvcTest(controllers = StatusController.class)
public class StatusControllerTest {

    //TODO: understand mocking (mockito) to i.e. mock the response from RestTemplate as we talked about last time
    //TODO: understand the lifecycles of maven, especially in terms of splitting Tests and IT (surefire vs failsafe)

    @TestConfiguration
    static class CustomizationConfiguration {

        @Bean
        public RestDocumentationResultHandler restDocumentation() {
            return MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                    preprocessResponse(prettyPrint(), removeHeaders("Content-Length")));
        }

    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirWatchStatusService service;

    @MockBean
    private ServiceStateService serviceStateService; // this is needed because of the service being autowired in service. Currently not being used.

    @Test
    public void getAdStatusWhenRequestingShouldReturnStatusResponseSuccess() throws Exception {
        StatusResponse responseSuccess = new StatusResponse();
        responseSuccess.setSuccess(true);
        responseSuccess.setTimestamp(Instant.now().getEpochSecond());
        responseSuccess.setMessage("Connection successful with the given Servername, Bind Username and Password.");

        /* TODO: figure out how i can use the document(responseFields) when using @AutoConfigureRestDocs
        FieldDescriptor[] book = new FieldDescriptor[] {
                fieldWithPath("IsSuccess").description("If the service state is healthy or not."),
                fieldWithPath("Message").description("Message describing the state of the service."),
                fieldWithPath("Timestamp").description("Timestamp in epoch format of when the service state was checked.")
            };
        */

        given(this.service.getDirectoryStatus())
                .willReturn(responseSuccess);

        mockMvc.perform(get("/status/ad").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IsSuccess", is(true)))
                .andExpect(jsonPath("$.Message", is("Connection successful with the given Servername, Bind Username and Password.")));
    }

    @Test
    public void getAdStatusWhenRequestingShouldReturnStatusResponseFailure() throws Exception {
        StatusResponse responseSuccess = new StatusResponse();
        responseSuccess.setSuccess(false);
        responseSuccess.setTimestamp(Instant.now().getEpochSecond());
        responseSuccess.setMessage("Error : Reached AWCM but Cloud Connector is not active.");

        given(this.service.getDirectoryStatus())
                .willReturn(responseSuccess);

        mockMvc.perform(get("/status/ad").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //TODO: implement the logic to handle failed status check to == HttpStatus 400
                .andExpect(jsonPath("$.IsSuccess", is(false)))
                .andExpect(jsonPath("$.Message", is("Error : Reached AWCM but Cloud Connector is not active.")));
    }

    @Test
    public void getAccStatusWhenRequestingShouldReturnStatusResponseSuccess() throws Exception {
        StatusResponse responseSuccess = new StatusResponse();
        responseSuccess.setSuccess(true);
        responseSuccess.setTimestamp(Instant.now().getEpochSecond());
        responseSuccess.setMessage("Cloud Connector is active.");

        given(this.service.getAccStatus())
                .willReturn(responseSuccess);

        mockMvc.perform(get("/status/acc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IsSuccess", is(true)))
                .andExpect(jsonPath("$.Message", is("Cloud Connector is active.")));
    }

    @Test
    public void getAccStatusWhenRequestingShouldReturnStatusResponseFailure() throws Exception {
        StatusResponse responseSuccess = new StatusResponse();
        responseSuccess.setSuccess(false);
        responseSuccess.setTimestamp(Instant.now().getEpochSecond());
        responseSuccess.setMessage("Error : Reached AWCM but Cloud Connector is not active.");

        given(this.service.getAccStatus())
                .willReturn(responseSuccess);

        mockMvc.perform(get("/status/acc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //TODO: implement the logic to handle failed status check to == HttpStatus 400
                .andExpect(jsonPath("$.IsSuccess", is(false)))
                .andExpect(jsonPath("$.Message", is("Error : Reached AWCM but Cloud Connector is not active.")));
    }

    @Test
    public void getStatusWhenInvalidHostnameShouldReturnHostNotFound() throws Exception {
        given(this.service.getAccStatus())
                .willThrow(HostNotFoundException.class);

        this.mockMvc.perform(get("/status/acc"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getStatusWhenInvalidCredentialsShouldReturnAuthenticationException() throws Exception {
        given(this.service.getAccStatus())
                .willThrow(AuthenticationException.class);

        this.mockMvc.perform(get("/status/acc"))
                .andExpect(status().isBadRequest());
    }
}