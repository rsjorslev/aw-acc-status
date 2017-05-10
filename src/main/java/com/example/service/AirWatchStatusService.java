package com.example.service;

import com.example.AwProperties;
import com.example.exception.AuthenticationException;
import com.example.model.ServiceName;
import com.example.model.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Slf4j
public class AirWatchStatusService {

    private final RestTemplate template;
    private final LoginService loginService;
    private final ServiceStateService stateService;
    private final AwProperties properties;
    private String accEndpoint;
    private String tenantUrl;
    private String adEndpoint;

    @Autowired
    public AirWatchStatusService(RestTemplate template, LoginService loginService, ServiceStateService stateService, AwProperties properties) {
        this.template = template;
        this.loginService = loginService;
        this.stateService = stateService;
        this.properties = properties;
        this.accEndpoint = properties.getUrl().getAccTest();
        this.tenantUrl = properties.getUrl().getTenant();
        this.adEndpoint = properties.getUrl().getDirectoryTest();
    }

    boolean isServiceStatusSuccess(StatusResponse response) {
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

    public StatusResponse getAccStatus() throws AuthenticationException {
        StatusResponse accTestResponse = this.getStatus(tenantUrl + accEndpoint);
        log.debug("ACC Test Response: {}", accTestResponse);
        stateService.runServiceStateCheck(accTestResponse, ServiceName.ACC);
        return accTestResponse;
    }

    public StatusResponse getDirectoryStatus() throws AuthenticationException {
        StatusResponse adTestResponse = this.getStatus(tenantUrl + adEndpoint);
        log.debug("Directory Test Response: {}", adTestResponse);
        stateService.runServiceStateCheck(adTestResponse, ServiceName.AD);
        return adTestResponse;
    }

    private StatusResponse getStatus(String endpoint) throws AuthenticationException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Cookie", loginService.getCookies());

        MultiValueMap<String, String> accMap = new LinkedMultiValueMap<>();
        accMap.add("__RequestVerificationToken", loginService.getRequestVerificationToken());

        HttpEntity httpEntity = new HttpEntity<>(accMap, headers);

        StatusResponse statusResponse = template.exchange(
                endpoint,
                HttpMethod.POST,
                httpEntity,
                StatusResponse.class
            ).getBody();

        // Add the current time to each returned StatusResponse
        statusResponse.setTimestamp(Instant.now().getEpochSecond());

        return statusResponse;
    }
}
