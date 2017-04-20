package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aw")
public class AwProperties {

    private final Url url = new Url();
    private final Login login = new Login();

    @Data
    public static class Url {
        private String tenant;
        private String login;
        private String loginUser;
        private String accTest;
        private String directoryTest;
        private String requestVerificationToken;
    }

    @Data
    public static class Login {
        private String user;
        @JsonIgnore // Don't show the password when serializing the AwProperties in AccController
        private String password;
    }

}