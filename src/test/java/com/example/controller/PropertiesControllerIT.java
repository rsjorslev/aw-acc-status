package com.example.controller;


import com.example.AwProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PropertiesControllerIT {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void getProperties() throws Exception {
        ResponseEntity<AwProperties> props = template.exchange(
                "/props",
                HttpMethod.GET,
                null,
                AwProperties.class);

        assertThat(props.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}
