package com.example.service;

import com.example.AwProperties;
import com.example.helpers.TokenTool;
import com.example.model.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AirWatchStatusService {

    private final AwProperties props;
    private final RestTemplate template;

    private final String USER_NAME;
    private final String PASSWORD;
    private final String TENANT_URL;
    private final String LOGIN_URL;
    private final String LOGIN_URL_USER;
    private final String TEST_ACC_CONNECTION;
    private final String TEST_DIRECTORY;
    private final String REQUEST_VERIFICATION_TOKEN;

    private Map sessionInfo = new HashMap();

    public AirWatchStatusService(AwProperties props, RestTemplate template) {
        this.props = props;
        this.template = template;
        USER_NAME = this.props.getLogin().getUser();
        PASSWORD = this.props.getLogin().getPassword();
        TENANT_URL = this.props.getUrl().getTenant();
        LOGIN_URL = TENANT_URL + this.props.getUrl().getLogin();
        LOGIN_URL_USER = TENANT_URL + this.props.getUrl().getLoginUser();
        TEST_ACC_CONNECTION = TENANT_URL + this.props.getUrl().getAccTest();
        TEST_DIRECTORY = TENANT_URL + this.props.getUrl().getDirectoryTest();
        REQUEST_VERIFICATION_TOKEN = TENANT_URL + this.props.getUrl().getRequestVerificationToken();
    }

    private String getLoginToken() {

        ResponseEntity<String> response = template.exchange(
                LOGIN_URL,
                HttpMethod.GET,
                null,
                String.class);

        String loginToken = TokenTool.extractRequestVerificationToken(response.getBody());
        log.debug("Login Token: {}", loginToken);

        return loginToken;
    }

    private String getRequestVerificationToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Requested-With", "XMLHttpRequest");
        HttpEntity tokenEntity = new HttpEntity(headers);

        ResponseEntity<String> tokenResponse = template.exchange(
                REQUEST_VERIFICATION_TOKEN,
                HttpMethod.GET,
                tokenEntity,
                String.class);

        log.debug("Request Verification Token response: {}", TokenTool.extractRequestVerificationToken(tokenResponse.getBody()));

        return TokenTool.extractRequestVerificationToken(tokenResponse.getBody());

    }

    /* Old implementation from when response was returned as String
    public boolean isServiceStatusSuccess(String responseBody) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(responseBody);

        try {
            Boolean isSuccess = JsonPath.read(document, "IsSuccess");
            if (!isSuccess) {
                return isSuccess;
            }
        } catch (PathNotFoundException pnfe) {
            log.debug("Not a valid JSON response");
            log.debug(pnfe.toString());
        }
        return true;
    }
    */

    public boolean isServiceStatusSuccess(StatusResponse response) {
        try {
            Boolean isSuccess = response.isSuccess();
            if (!isSuccess) {
                return isSuccess;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return true;
    }

    private String getCookies() {
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
        map.add("UserName", USER_NAME);
        map.add("Password", PASSWORD);
        map.add("X-Requested-With", "XMLHttpRequest");

        HttpEntity<?> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<String> loginResponse = template.exchange(
                LOGIN_URL_USER,
                HttpMethod.POST,
                httpEntity,
                String.class);

        String cookies = loginResponse.getHeaders().get("Set-Cookie").stream().collect(Collectors.joining(";"));

        this.sessionInfo.put("cookie", cookies);
        this.sessionInfo.put("timestamp", Instant.now());

        log.debug("Cookies returned: {}", cookies);

        return cookies;

    }

    public StatusResponse getAccStatus() {
        HttpHeaders accTestHeaders = new HttpHeaders();
        accTestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        accTestHeaders.add("Cookie", this.getCookies());

        MultiValueMap<String, String> accMap = new LinkedMultiValueMap<>();
        accMap.add("__RequestVerificationToken", this.getRequestVerificationToken());

        HttpEntity accEntity = new HttpEntity<>(accMap, accTestHeaders);

        ResponseEntity<StatusResponse> accTestResponse = template.exchange(
                TEST_ACC_CONNECTION,
                HttpMethod.POST,
                accEntity,
                StatusResponse.class
        );

        if (this.isServiceStatusSuccess(accTestResponse.getBody())) {
            log.info("ACC service is running and connected to AWCM");
        } else {
            log.warn("ACC service is not connected. Message returned from status check: {}", accTestResponse.getBody().getMessage());
        }

        log.debug("ACC Test Response: {}", accTestResponse.getBody());

        return accTestResponse.getBody();

    }

    public StatusResponse getDirectoryStatus() {
        HttpHeaders accTestHeaders = new HttpHeaders();
        accTestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        accTestHeaders.add("Cookie", this.getCookies());

        MultiValueMap<String, String> accMap = new LinkedMultiValueMap<>();
        accMap.add("__RequestVerificationToken", this.getRequestVerificationToken());

        HttpEntity accEntity = new HttpEntity<>(accMap, accTestHeaders);

        ResponseEntity<StatusResponse> accTestResponse = template.exchange(
                TEST_DIRECTORY,
                HttpMethod.POST,
                accEntity,
                StatusResponse.class
        );

        log.debug("Directory Test Response: {}", accTestResponse.getBody());

        return accTestResponse.getBody();

    }

}
