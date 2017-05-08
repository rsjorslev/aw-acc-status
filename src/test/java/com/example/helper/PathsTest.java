package com.example.helper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Paths}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PathsTest {

    private String TENANT_URL;
    private String USER_NAME;
    private String PASSWORD;
    private String LOGIN_URL = "/AirWatch/Login";
    private String LOGIN_URL_USER = "/AirWatch/login/Login/Login-User";
    private String TEST_ACC_CONNECTION = "/AirWatch/Settings/TestAccConnection";
    private String TEST_DIRECTORY = "/AirWatch/Settings/TestDirectoryConnection";
    private String REQUEST_VERIFICATION_TOKEN = "/AirWatch/Branding";

    @Before
    public void setUp() {
        TENANT_URL = "https://aw.example.com";
        USER_NAME =  "user@example.com";
        PASSWORD = "Myl1ttl3P0ny";
    }

    @Test
    @Ignore //TODO: figure out how to avoid hardcoding actual user//pass for tests so this test can run again
    public void testPropertiesAreCorrect() throws Exception {

        assertThat(Paths.TENANT_URL).isEqualTo(TENANT_URL);
        assertThat(Paths.LOGIN_URL).isEqualTo(TENANT_URL + LOGIN_URL);
        assertThat(Paths.LOGIN_URL_USER).isEqualTo(TENANT_URL + LOGIN_URL_USER);
        assertThat(Paths.USER_NAME).isEqualTo(USER_NAME);
        assertThat(Paths.PASSWORD).isEqualTo(PASSWORD);
        assertThat(Paths.TEST_ACC_CONNECTION).isEqualTo(TENANT_URL + TEST_ACC_CONNECTION);
        assertThat(Paths.TEST_DIRECTORY).isEqualTo(TENANT_URL + TEST_DIRECTORY);
        assertThat(Paths.REQUEST_VERIFICATION_TOKEN).isEqualTo(TENANT_URL + REQUEST_VERIFICATION_TOKEN);

    }
}