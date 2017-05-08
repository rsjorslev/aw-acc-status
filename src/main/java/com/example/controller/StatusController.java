package com.example.controller;

import com.example.exception.AuthenticationException;
import com.example.exception.HostNotFoundException;
import com.example.model.ServiceName;
import com.example.model.ServiceStateDetails;
import com.example.model.StatusResponse;
import com.example.service.AirWatchStatusService;
import com.example.service.ServiceStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/status")
public class StatusController {

    private final AirWatchStatusService service;
    private final ServiceStateService stateService;

    @Autowired
    public StatusController(AirWatchStatusService service, ServiceStateService stateService) {
        this.service = service;
        this.stateService = stateService;
    }

    @RequestMapping(value = "/acc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StatusResponse getAccStatus() throws AuthenticationException {
        return service.getAccStatus();
    }

    @RequestMapping(value = "/ad", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StatusResponse getAdStatus() throws AuthenticationException {
        return service.getDirectoryStatus();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<ServiceName, ServiceStateDetails> listServiceStates() {
        return stateService.getServiceStateDetails();
    }

    /* TODO: implement
    @RequestMapping(value = "/all/isOk", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean isAllServicesOk() {
        stateService.isAllServicesOk();
        return true;
    }
    */

}
