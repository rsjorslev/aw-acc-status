package com.example;

import com.example.helpers.TokenTool;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TokenTool}
 */
public class ExtractTokenFromFileTest {

    private String TOKEN_EXPECTED = "hCnZmPGck-lVEbS5ZD-XNuUT1cU_XGKJjg26BvdLaCtXjgItdi4TH_TC6uFxP3uoMPKGbclekCV_K8CY11hTa2g5MP1n4L8o7m7PYYiOLy81";
    private String TOKEN_ACTUAL;

    @Before
    public void setUp() throws Exception {
        Resource resource = new ClassPathResource("AWLoginPage.html");
        String contents = new String(Files.readAllBytes(resource.getFile().toPath()));

        TOKEN_ACTUAL = TokenTool.extractRequestVerificationToken(contents);
    }

    @Test
    public void extractTokenFromLoginPage() throws Exception {

        assertThat(TOKEN_ACTUAL).isEqualTo(TOKEN_EXPECTED);

    }
}
