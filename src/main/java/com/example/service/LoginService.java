package com.example.service;

import com.example.AwProperties;
import com.example.exception.AuthenticationException;
import com.example.exception.HostNotFoundException;
import com.example.helper.TokenTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final AwProperties properties;

    private Map sessionInfo = new HashMap();
    private String tenantUrl;
    private String loginPath;
    private String loginUserPath;
    private String tokenPath;

    @Autowired
    public LoginService(RestTemplate template, AwProperties properties) {
        this.template = template;
        this.properties = properties;
        this.tenantUrl = properties.getUrl().getTenant();
        this.loginPath = properties.getUrl().getLogin();
        this.loginUserPath = properties.getUrl().getLoginUser();
        this.tokenPath = properties.getUrl().getRequestVerificationToken();
    }

    String getLoginToken() {

        ResponseEntity<String> response = null;
        try {
            response = template.exchange(
                    tenantUrl + loginPath,
                    HttpMethod.GET,
                    null,
                    String.class);
        }

        catch (RestClientException e) {
            if (e.getCause() instanceof UnknownHostException) {
                log.error("UnknownHostException occurred: " + e);
                throw new HostNotFoundException("Could not connect to the provided AW address: " + e.getRootCause().getMessage(), e.getCause());
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
        map.add("UserName", properties.getLogin().getUser());
        map.add("Password", properties.getLogin().getPassword());
        map.add("X-Requested-With", "XMLHttpRequest");

        HttpEntity<?> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<String> loginResponse = template.exchange(
                tenantUrl + loginUserPath,
                HttpMethod.POST,
                httpEntity,
                String.class);

        log.debug("Login Response headers: {}", loginResponse.getHeaders());

        String cookies = loginResponse.getHeaders().get("Set-Cookie").stream().collect(Collectors.joining(";"));

        //TODO: check if it makes more sense to throw AuthenticationException if cookie size == 1 as
        log.debug("Size of the returned Set-Cookie header from getCookies(): {}", loginResponse.getHeaders().get("Set-Cookie").size());

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
                tenantUrl + tokenPath,
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
