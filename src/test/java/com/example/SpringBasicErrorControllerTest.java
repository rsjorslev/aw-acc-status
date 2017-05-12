package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Mock of {@link org.springframework.boot.autoconfigure.web.BasicErrorController}
 * This test is only used to document the error response.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = BasicErrorController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost = "example.com")
@AutoConfigureMockMvc
public class SpringBasicErrorControllerTest {

    @TestConfiguration
    static class CustomizationConfiguration {
        @Bean
        public RestDocumentationResultHandler restDocumentation() {
            return MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                    preprocessResponse(prettyPrint(), removeHeaders("Content-Length")));
        }
    }

    private RestDocumentationResultHandler documentationHandler;

    @Before
    public void setUp() {
        this.documentationHandler = document(
                "{ClassName}/{methodName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(),
                removeHeaders("Content-Length"))
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void errorExample() throws Exception {
        this.mockMvc
                .perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/status/acc")
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "A RequestVerificationToken could not be retrieved. Please verify that the provided username and password are correct."))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", is("Bad Request")))
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is(notNullValue())))
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                fieldWithPath("message").description("A description of the cause of the error"),
                                fieldWithPath("path").description("The path to which the request was made"),
                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
    }
}
