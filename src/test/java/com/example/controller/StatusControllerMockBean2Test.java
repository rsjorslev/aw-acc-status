package com.example.controller;

import com.example.exception.AuthenticationException;
import com.example.exception.HostNotFoundException;
import com.example.model.StatusResponse;
import com.example.service.AirWatchStatusService;
import com.example.service.ServiceStateService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.RequestDispatcher;
import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(StatusController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@Ignore
public class StatusControllerMockBean2Test {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AirWatchStatusService service;

    @MockBean
    private ServiceStateService serviceStateService;

    @Test
    public void name() throws Exception {

        given(this.service.getAccStatus())
                .willReturn(new StatusResponse(true, "success", Instant.now().getEpochSecond()));

        this.mvc.perform(get("/status/acc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mock-test", preprocessResponse(prettyPrint())));
    }

    @Test
    public void test2() throws Exception {
        given(this.service.getAccStatus())
                .willThrow(AuthenticationException.class);

        this.mvc.perform(get("/status/acc")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("host-not-found"));
    }

    @Test
    public void throwsShouldShowResponseBody() throws Exception {
        given(this.service.getAccStatus())
                .willThrow(AuthenticationException.class);

        this.mvc.perform(get("/status/throw/2"))
                .andExpect(status().isBadRequest())
                .andDo(document("bad-credentials"));
    }

    @Test
    public void pageNotFound() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get("/no/page")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
            .andDo(print())
            .andExpect(status().isNotFound());

    }

    @Test
    public void errorExample() throws Exception {
        this.mvc
                .perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI,
                                "/notes")
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE,
                                "this does not exist")
                .requestAttr(RequestDispatcher.ERROR_EXCEPTION_TYPE, HostNotFoundException.class))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", is("Bad Request")))
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is(notNullValue())))
                .andDo(document("error-example",
                        responseFields(
                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                fieldWithPath("message").description("A description of the cause of the error"),
                                fieldWithPath("path").description("The path to which the request was made"),
                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
    }
}