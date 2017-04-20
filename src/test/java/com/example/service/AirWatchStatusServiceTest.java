package com.example.service;

import com.example.model.StatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AirWatchStatusService}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AirWatchStatusServiceTest {

    @Autowired
    private AirWatchStatusService service;

    private final String JSON_RESPONSE_FAILED   = "{\"IsSuccess\": false, \"Message\": \"Cloud Connector is not connected to AWCM.\"}";
    private final String JSON_RESPONSE_SUCCESS  = "{\"IsSuccess\": true, \"Message\": \"Cloud Connector is active.\"}";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getAccStatus() throws Exception {
    }

    @Test
    public void getDirectoryStatus() throws Exception {
    }

    @Test
    public void serviceStatusSuccessTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        assertThat(service.isServiceStatusSuccess(mapper.readValue(JSON_RESPONSE_SUCCESS, StatusResponse.class))).isTrue();
        assertThat(service.isServiceStatusSuccess(mapper.readValue(JSON_RESPONSE_FAILED, StatusResponse.class))).isFalse();
    }
}