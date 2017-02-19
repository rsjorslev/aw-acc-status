package com.example.controller;

import com.example.AwProperties;
import com.example.service.AirWatchStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/status")
public class AccController {

    private final AwProperties props;
    private final AirWatchStatusService service;

    @Autowired
    public AccController(AwProperties props, AirWatchStatusService service) {
        this.props = props;
        this.service = service;
    }

    @RequestMapping(value = "/acc")
    public String accStatus() {
        return service.getAccStatus();
    }

    @RequestMapping(value = "/ad")
    public String adStatus() {
        return service.getDirectoryStatus();
    }

    @RequestMapping(value = "/props", method = RequestMethod.GET)
    public AwProperties getProps() {
        return props;
    }

}
