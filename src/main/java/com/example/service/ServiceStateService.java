package com.example.service;

import com.example.model.ServiceName;
import com.example.model.ServiceStateDetails;
import com.example.model.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ServiceStateService {

    private Map<ServiceName, ServiceStateDetails> details = new HashMap<>();

    public void runServiceStateCheck(StatusResponse statusResponse, ServiceName serviceName) {

        ServiceStateDetails ssd;

        if (details.containsKey(serviceName)) {
            ssd = details.get(serviceName);
        } else {
            ssd = new ServiceStateDetails();
            details.put(serviceName, ssd);
        }
            ssd.setSuccess(statusResponse.isSuccess());
            ssd.setMessage(statusResponse.getMessage());
            ssd.setServiceName(serviceName.name());
            ssd.setTimestamp(Instant.now());
    }

    public Map<ServiceName, ServiceStateDetails> getServiceStateDetails() {
        return this.details;
    }

    public boolean isAllServicesOk() {
        ArrayList<Boolean> bools = new ArrayList<>();

        details.entrySet().stream().forEach(x -> System.out.println("IsSuccess: " + x.getValue().isSuccess()));

        details.entrySet().forEach((x -> bools.add(x.getValue().isSuccess())));

        System.out.println("Bools size: " + bools.size());
        bools.stream().forEach(x -> System.out.println("Bools IsSuccess: " + x.toString()));

        return bools.stream().anyMatch(x -> false);
    }

}
