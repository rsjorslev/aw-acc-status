package com.example.model;

import lombok.Data;

import java.time.Instant;

@Data
public class ServiceStateDetails {

    private String serviceName;
    private String message;
    private boolean isSuccess;
    private Instant timestamp;
    private Instant lastSuccess;
    private Instant lastFailure;

}
