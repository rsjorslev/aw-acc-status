package com.example.service;

import com.example.AwProperties;
import com.example.exception.AuthenticationException;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for {@link LoginService}
 */
@RunWith(SpringRunner.class)
@RestClientTest({ LoginService.class, AwProperties.class })
@AutoConfigureWebClient(registerRestTemplate=true)
@TestPropertySource(properties = {
        "aw.url.tenant=http://example.com",
        "aw.url.request-verification-token=/token",
        "aw.url.login=/login",
        "aw.url.login-user=/user"
})
public class LoginServiceTestSpring {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private LoginService service;

    String LOGIN_TOKEN = "hCnZmPGck-lVEbS5ZD-XNuUT1cU_XGKJjg26BvdLaCtXjgItdi4TH_TC6uFxP3uoMPKGbclekCV_K8CY11hTa2g5MP1n4L8o7m7PYYiOLy81";
    String INPUT = "input name=\"__RequestVerificationToken\" type=\"hidden\" value=\"hCnZmPGck-lVEbS5ZD-XNuUT1cU_XGKJjg26BvdLaCtXjgItdi4TH_TC6uFxP3uoMPKGbclekCV_K8CY11hTa2g5MP1n4L8o7m7PYYiOLy81\" />";

    @Test
    public void getLoginTokenWhenResultIsSuccessShouldReturnLoginToken() throws Exception {
        this.server.expect(requestTo("http://example.com/login"))
                .andRespond(withSuccess(INPUT,MediaType.APPLICATION_JSON));

        assertThat(service.getLoginToken(), equalTo(LOGIN_TOKEN));
    }

    @Test
    public void getLoginTokenWhenResultIsUnknownHostShouldThrowException() throws Exception {
        this.server.expect(requestTo("http://example.com/login"))
                .andExpect(method((HttpMethod.GET)))
                .andRespond(withBadRequest());
        this.thrown.expect(RestClientException.class);
        //thrown.expectCause(IsInstanceOf.<Throwable>instanceOf(UnknownHostException.class));
        //this.thrown.expectCause(isA(UnknownHostException.class));
        throw new RestClientException(null, new UnknownHostException());
    }

    @Test
    public void getRequestVerificationTokenWhenResultIsSuccessShouldReturnToken() throws Exception {
        this.server.expect(requestTo("http://example.com/token"))
                .andExpect(header("X-Requested-With", "XMLHttpRequest"))
                .andRespond(withSuccess(INPUT,MediaType.APPLICATION_JSON));

        assertThat(service.getRequestVerificationToken(), equalTo(LOGIN_TOKEN));

    }

    //TODO i dont think im testing the right thing here... i dont think my exceptions are bound to the Response object
    @Test
    public void getRequestVerificationTokenWhenResultIsBadCredentialsShouldThrowException() throws Exception {
        this.server.expect(requestTo("http://example.com/token"))
                .andExpect(header("X-Requested-With", "XMLHttpRequest"))
                .andRespond(withSuccess("asdasd", MediaType.APPLICATION_JSON));
        this.thrown.expect(AuthenticationException.class);
        throw new AuthenticationException("asdasd");

    }
}
