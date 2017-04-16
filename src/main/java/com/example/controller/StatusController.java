package com.example.controller;

import com.example.model.StatusResponse;
import com.example.service.AirWatchStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/status")
public class StatusController {

    private final AirWatchStatusService service;

    @Autowired
    public StatusController(AirWatchStatusService service) {
        this.service = service;
    }

    @RequestMapping(value = "/acc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StatusResponse accStatus() {
        return service.getAccStatus();
    }

    @RequestMapping(value = "/ad", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StatusResponse adStatus() {
        return service.getDirectoryStatus();
    }

}
