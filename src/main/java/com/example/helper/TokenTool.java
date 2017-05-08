package com.example.helper;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TokenTool {

    public static String extractRequestVerificationToken(String input) {

        log.trace("html input as passed into method: {}", input);

        String PATTERN = "name=\"__RequestVerificationToken\" type=\"hidden\" value=\"(.*?)\"";
        String token = null;

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            token = matcher.group(1);
        }

        return token;
    }

    public static boolean isCookieValid(Instant instant) {
        Instant twentyMinAgo = Instant.now().minus(Duration.ofMinutes(20L));
        log.debug("Defining 20 min ago as: {}", twentyMinAgo);

        if (instant.isAfter(twentyMinAgo)) {
            log.debug("Cookie still valid");
            return true;
        } else {
            log.debug("Cookie stored is no longer valid as it is more than 20 minutes old. Timestamp given was: {}", instant);
            return false;
        }
    }
}
