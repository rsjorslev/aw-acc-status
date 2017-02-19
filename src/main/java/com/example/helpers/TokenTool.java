package com.example.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenTool {

    public static String extractRequestVerificationToken(String input) {

        String PATTERN = "name=\"__RequestVerificationToken\" type=\"hidden\" value=\"(.*?)\"";
        String token = null;

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            token = matcher.group(1);
        }

        return token;
    }

}
