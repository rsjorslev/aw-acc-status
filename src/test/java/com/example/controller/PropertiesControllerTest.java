package com.example.controller;

import com.example.AwProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for {@link PropertiesController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = { PropertiesController.class, AwProperties.class })
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost = "example.com")
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "aw.login.user=user@example.com",
        "aw.url.tenant=https://example.com",
})
public class PropertiesControllerTest {

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

    @Test
    public void testPropertiesEndpointAndExpectStatus200Response() throws Exception {
        mockMvc.perform(get("/props").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url.tenant", is("https://example.com")))
                .andExpect(jsonPath("$.url.login", is("/AirWatch/Login")))
                .andExpect(jsonPath("$.url.loginUser", is("/AirWatch/login/Login/Login-User")))
                .andExpect(jsonPath("$.url.accTest", is("/AirWatch/Settings/TestAccConnection")))
                .andExpect(jsonPath("$.url.directoryTest", is("/AirWatch/Settings/TestDirectoryConnection")))
                .andExpect(jsonPath("$.url.requestVerificationToken", is("/AirWatch/Branding")))
                .andExpect(jsonPath("$.login.user", is("user@example.com")));
    }

}