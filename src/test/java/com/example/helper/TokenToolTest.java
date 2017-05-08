package com.example.helper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TokenTool}
 */
public class TokenToolTest {

    private String TOKEN_ACTUAL;
    private String TOKEN_EXPECTED           = "hCnZmPGck-lVEbS5ZD-XNuUT1cU_XGKJjg26BvdLaCtXjgItdi4TH_TC6uFxP3uoMPKGbclekCV_K8CY11hTa2g5MP1n4L8o7m7PYYiOLy81";
    private Instant SEVEN_MINUTES_AGO       = Instant.now().minus(Duration.ofMinutes(7));
    private Instant TWENTY_MINUTES_AGO      = Instant.now().minus(Duration.ofMinutes(20));
    private Instant TWENTY_TWO_MINUTES_AGO  = Instant.now().minus(Duration.ofMinutes(22));

    //TODO: This runs before every test but is only needed for testTokenCanBeExtractedFromAWLoginPage - how can i change/optimize that?
    @Before
    public void setUp() throws Exception {
        Resource resource = new ClassPathResource("AWLoginPage.html");
        String contents = new String(Files.readAllBytes(resource.getFile().toPath()));
        TOKEN_ACTUAL = TokenTool.extractRequestVerificationToken(contents);
    }

    @Test
    public void testTokenCanBeExtractedFromAWLoginPage() throws Exception {

        assertThat(TOKEN_ACTUAL).isEqualTo(TOKEN_EXPECTED);

    }

    @Test
    public void testTimestampInSessionStateExpiresAfter20Minutes() throws Exception {

        assertThat(TokenTool.isCookieValid(SEVEN_MINUTES_AGO)).isTrue();
        assertThat(TokenTool.isCookieValid(TWENTY_MINUTES_AGO)).isFalse();
        assertThat(TokenTool.isCookieValid(TWENTY_TWO_MINUTES_AGO)).isFalse();

    }
}