package com.example.service;

import com.example.exceptions.AuthenticationException;
import com.example.helpers.Paths;
import com.example.helpers.TokenTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginService {

    private final RestTemplate template;

    private Map sessionInfo = new HashMap();

    public LoginService(RestTemplate template) {
        this.template = template;
    }

    private String getLoginToken() {

        ResponseEntity<String> response = null;
        try {
            response = template.exchange(
                    Paths.LOGIN_URL,
                    HttpMethod.GET,
                    null,
                    String.class);
        }

        //TODO: understand how i can throw a custom exception inside the UnknownHostException if
        catch (RestClientException e) {
            log.debug("inside RestClientException");
            if (e.getCause() instanceof UnknownHostException) {
                log.debug("inside UnknownHostException");
            }
            throw e;
        }

        log.trace("getLoginToken response body: {}", response.getBody());

        String loginToken = TokenTool.extractRequestVerificationToken(response.getBody());
        log.debug("Login Token: {}", loginToken);

        return loginToken;
    }

    public String getCookies() {
        Instant timeStamp = (Instant) this.sessionInfo.get("timestamp");

        if (timeStamp != null && TokenTool.isCookieValid(timeStamp)) {
            log.debug("Cookie stored in sessionInfo is valid - returning the stored value.");
            return (String) this.sessionInfo.get("cookie");
        }

        log.debug("No cookie found in sessionInfo or cookie no longer valid. Performing new login request to acquire new cookie");

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("__RequestVerificationToken", this.getLoginToken());
        map.add("UserName", Paths.USER_NAME);
        map.add("Password", Paths.PASSWORD);
        map.add("X-Requested-With", "XMLHttpRequest");

        HttpEntity<?> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<String> loginResponse = template.exchange(
                Paths.LOGIN_URL_USER,
                HttpMethod.POST,
                httpEntity,
                String.class);

        String cookies = loginResponse.getHeaders().get("Set-Cookie").stream().collect(Collectors.joining(";"));

        //TODO: check if it makes more sense to throw AuthenticationException if cookie size == 1 as
        // it would then be invalid. It needs to be 6 after successful login
        log.debug("size of cookie header: {}", loginResponse.getHeaders().get("Set-Cookie").size());

        this.sessionInfo.put("cookie", cookies);
        this.sessionInfo.put("timestamp", Instant.now());

        log.debug("Cookies returned: {}", cookies);

        return cookies;

    }

    String getRequestVerificationToken() throws AuthenticationException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Requested-With", "XMLHttpRequest");
        HttpEntity tokenEntity = new HttpEntity(headers);

        //TODO: come back to this and try/catch handle logic in case of server name given not correct
        ResponseEntity<String> tokenResponse = template.exchange(
                Paths.REQUEST_VERIFICATION_TOKEN,
                HttpMethod.GET,
                tokenEntity,
                String.class);

        String requestToken = TokenTool.extractRequestVerificationToken(tokenResponse.getBody());

        if (requestToken == null) {
            throw new AuthenticationException("A RequestVerificationToken could not be retrieved. Please verify that the provided username and password are correct.");
        }

        log.debug("Request Verification Token response: {}", requestToken);

        return requestToken;
    }


}
