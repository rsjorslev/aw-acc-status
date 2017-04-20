package com.example.service;

import com.example.exceptions.AuthenticationException;
import com.example.helpers.Paths;
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

@Service
@Slf4j
public class AirWatchStatusService {

    private final RestTemplate template;
    private final LoginService loginService;

    public AirWatchStatusService(RestTemplate template, LoginService loginService) {
        this.template = template;
        this.loginService = loginService;
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
        ResponseEntity<StatusResponse> accTestResponse = this.getStatus(Paths.TEST_ACC_CONNECTION);
        log.debug("ACC Test Response: {}", accTestResponse.getBody());
        return accTestResponse.getBody();
    }

    public StatusResponse getDirectoryStatus() throws AuthenticationException {
        ResponseEntity<StatusResponse> adTestResponse = this.getStatus(Paths.TEST_DIRECTORY);
        log.debug("Directory Test Response: {}", adTestResponse.getBody());
        return adTestResponse.getBody();
    }

    private ResponseEntity<StatusResponse> getStatus(String endpoint) throws AuthenticationException {
        HttpHeaders accTestHeaders = new HttpHeaders();
        accTestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        accTestHeaders.add("Cookie", loginService.getCookies());

        MultiValueMap<String, String> accMap = new LinkedMultiValueMap<>();
        accMap.add("__RequestVerificationToken", loginService.getRequestVerificationToken());

        HttpEntity accEntity = new HttpEntity<>(accMap, accTestHeaders);

        return template.exchange(
                endpoint,
                HttpMethod.POST,
                accEntity,
                StatusResponse.class
        );
    }
}
