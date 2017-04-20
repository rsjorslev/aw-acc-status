package com.example.helpers;

import com.example.AwProperties;
import org.springframework.stereotype.Component;

@Component
public class Paths {

    private final AwProperties props;

    private static String TENANT_URL;
    public static String TEST_ACC_CONNECTION;
    public static String TEST_DIRECTORY;
    public static String USER_NAME;
    public static String PASSWORD;
    public static String LOGIN_URL_USER;
    public static String LOGIN_URL;
    public static String REQUEST_VERIFICATION_TOKEN;

    public Paths(AwProperties props) {
        this.props = props;
        USER_NAME = this.props.getLogin().getUser();
        PASSWORD = this.props.getLogin().getPassword();
        TENANT_URL = this.props.getUrl().getTenant();
        LOGIN_URL = TENANT_URL + this.props.getUrl().getLogin();
        LOGIN_URL_USER = TENANT_URL + this.props.getUrl().getLoginUser();
        REQUEST_VERIFICATION_TOKEN = TENANT_URL + this.props.getUrl().getRequestVerificationToken();
        TENANT_URL = this.props.getUrl().getTenant();
        TEST_ACC_CONNECTION = TENANT_URL + this.props.getUrl().getAccTest();
        TEST_DIRECTORY = TENANT_URL + this.props.getUrl().getDirectoryTest();
    }
}
