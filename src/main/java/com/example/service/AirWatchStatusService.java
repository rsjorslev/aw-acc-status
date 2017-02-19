package com.example.service;

import com.example.AwProperties;
import com.example.helpers.TokenTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    private final String CLOUD_CONNECTOR;
    private final String TEST_ACC_CONNECTION;
    private final String DIRECTORY_SERVICE;
    private final String TEST_DIRECTORY;

    public AirWatchStatusService(AwProperties props, RestTemplate template) {
        this.props = props;
        this.template = template;
        USER_NAME = this.props.getLogin().getUser();
        PASSWORD = this.props.getLogin().getPassword();
        TENANT_URL = this.props.getUrl().getTenant();
        LOGIN_URL = TENANT_URL + this.props.getUrl().getLogin();
        LOGIN_URL_USER = TENANT_URL + this.props.getUrl().getLoginUser();
        CLOUD_CONNECTOR = TENANT_URL + this.props.getUrl().getCloudConnector();
        TEST_ACC_CONNECTION = TENANT_URL + this.props.getUrl().getAccTest();
        DIRECTORY_SERVICE = TENANT_URL + this.props.getUrl().getDirectoryService();
        TEST_DIRECTORY = TENANT_URL + this.props.getUrl().getDirectoryTest();
    }

    private String getLoginToken() {

        ResponseEntity<String> response = template.exchange(
                LOGIN_URL,
                HttpMethod.GET,
                null,
                String.class);

        log.debug("Login Token: {}", TokenTool.extractRequestVerificationToken(response.getBody()));

         return TokenTool.extractRequestVerificationToken(response.getBody());
    }

    private String getCloudConnectorToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Requested-With", "XMLHttpRequest");
        HttpEntity ccEntity = new HttpEntity(headers);

        ResponseEntity<String> ccResponse = template.exchange(
                CLOUD_CONNECTOR,
                HttpMethod.GET,
                ccEntity,
                String.class);

        log.debug("CC Response Token: {}", TokenTool.extractRequestVerificationToken(ccResponse.getBody()));

        return TokenTool.extractRequestVerificationToken(ccResponse.getBody());

    }

    private String getDirectoryToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Requested-With", "XMLHttpRequest");
        HttpEntity ccEntity = new HttpEntity(headers);

        ResponseEntity<String> ccResponse = template.exchange(
                DIRECTORY_SERVICE,
                HttpMethod.GET,
                ccEntity,
                String.class);

        log.debug("Directory Response Token: {}", TokenTool.extractRequestVerificationToken(ccResponse.getBody()));

        return TokenTool.extractRequestVerificationToken(ccResponse.getBody());

    }

    private String getCookies() {

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

        log.debug("Cookies returned: {}", cookies);

        return cookies;

    }

    public String getAccStatus() {
        HttpHeaders accTestHeaders = new HttpHeaders();
        accTestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        accTestHeaders.add("Cookie", this.getCookies());

        MultiValueMap<String, String> accMap = new LinkedMultiValueMap<>();
        accMap.add("__RequestVerificationToken", this.getCloudConnectorToken());

        HttpEntity accEntity = new HttpEntity<>(accMap, accTestHeaders);

        ResponseEntity<String> accTestResponse = template.exchange(
                TEST_ACC_CONNECTION,
                HttpMethod.POST,
                accEntity,
                String.class
        );

        log.debug("ACC Test Response: {}", accTestResponse.getBody());

        return accTestResponse.getBody();

    }

    public String getDirectoryStatus() {
        HttpHeaders accTestHeaders = new HttpHeaders();
        accTestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        accTestHeaders.add("Cookie", this.getCookies());

        MultiValueMap<String, String> accMap = new LinkedMultiValueMap<>();
        accMap.add("__RequestVerificationToken", this.getDirectoryToken());

        HttpEntity accEntity = new HttpEntity<>(accMap, accTestHeaders);

        ResponseEntity<String> accTestResponse = template.exchange(
                TEST_DIRECTORY,
                HttpMethod.POST,
                accEntity,
                String.class
        );

        log.debug("Directory Test Response: {}", accTestResponse.getBody());

        return accTestResponse.getBody();

    }

}
