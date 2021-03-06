package com.example.controller;

import com.example.AwProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/props")
public class PropertiesController {

    private final AwProperties props;

    @Autowired
    public PropertiesController(AwProperties props) {
        this.props = props;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AwProperties getProps() {
        return props;
    }
}
