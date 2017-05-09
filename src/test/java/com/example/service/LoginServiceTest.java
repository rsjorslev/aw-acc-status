package com.example.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    RestTemplate template;
    LoginService service;
    String LOGIN_TOKEN = "om9vxOmplP4pJnbJ8mqZrZ_U3Ag6SwO0SNuknKFc1HKezkqbOO9jmXfyeN139KgUFdI28h1PGJzgg0HbP6DjUPfkH9R3WU2gImxEgZO1_zU1";
    String INPUT = "input name=\"__RequestVerificationToken\" type=\"hidden\" value=\"hCnZmPGck-lVEbS5ZD-XNuUT1cU_XGKJjg26BvdLaCtXjgItdi4TH_TC6uFxP3uoMPKGbclekCV_K8CY11hTa2g5MP1n4L8o7m7PYYiOLy81\" />";

    @Before
    public void setUp() {
        template = Mockito.mock(RestTemplate.class);
        service = new LoginService(template);
    }

    @Test
    public void getLoginTokenTest() throws Exception {

        when(template.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class)
                )).thenReturn(new ResponseEntity<>(INPUT, HttpStatus.OK));

        assertThat(service.getLoginToken(), equalTo(LOGIN_TOKEN));

    }
}
