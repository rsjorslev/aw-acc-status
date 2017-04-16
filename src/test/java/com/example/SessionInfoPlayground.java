package com.example;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SessionInfoPlayground {

    String COOKIES = "BrandingUserLocationGroupID=9FHi+uhCAgcky/pIHmGNqA==; expires=Fri, 12-May-2017 13:45:58 GMT; path=/; secure; HttpOnly;GloblizationUserCultureID=IXcVPYiENWr4C55IUIysCQ==; expires=Fri, 12-May-2017 13:45:58 GMT; path=/; secure; HttpOnly;GloblizationUserLocationGroupID=enzQnlsV5M2qUeAu2pXQ7g==; expires=Fri, 12-May-2017 13:45:58 GMT; path=/; secure; HttpOnly;ASP.NET_SessionId=e32igfwkekv5rewqeqbwsguz; path=/; secure; HttpOnly;.AIRWATCHAUTH=985FCBBCEA9C57C1AFC30D2FC11611B0DAFC5314A8EF7675C880B29249D8E706073B1CA2521089D7144FCC325DB69893DE983B26D8F566B04DB790D815EB5B7A92A45A10863873A45859DB5B2419805E1A7999760CDF623AE4A53BC1FF8DC07C2740A43AF4C8986148C8287B08132203; expires=Thu, 13-Apr-2017 13:45:58 GMT; path=/; secure; HttpOnly;__RequestVerificationToken_L0FpcldhdGNo0=01T0QiWofHOuuqZEDxK9CfqEIvtZq1Bj1lduMN8FZtGT3q1yqq4GGgiiXc89SbY8Q8VQhEP7Pc2JaYhTVvRl3ydlAsG3kACDddGW3kIEv7E1; path=/; secure; HttpOnly";
    Instant sevenMinutesAgo = Instant.now().minus(Duration.ofMinutes(7));
    Instant twentyTwoMinutesAgo = Instant.now().minus(Duration.ofMinutes(22));
    Instant twentyMinAgo = Instant.now().minus(Duration.ofMinutes(20));
    Instant epochTest = Instant.ofEpochSecond(1492022407);

    private boolean isLoginCookieValid(Instant instant) {

        System.out.println("sevenMinutesAgo: " + sevenMinutesAgo);
        System.out.println("twentyTwoMinutesAgo: " + twentyTwoMinutesAgo);
        System.out.println("twentyMinAgo: " + twentyMinAgo);
        System.out.println("epochTest: " + epochTest);

        System.out.println(instant.isBefore(sevenMinutesAgo));
        System.out.println(instant.isBefore(twentyTwoMinutesAgo));
        System.out.println(instant.isBefore(twentyMinAgo));
        System.out.println();

        System.out.println(instant.isAfter(sevenMinutesAgo));
        System.out.println(instant.isAfter(twentyTwoMinutesAgo));
        System.out.println(instant.isAfter(twentyMinAgo));

        return true;
    }

    @Test
    public void name() throws Exception {

        Map sessionInfo = new HashMap();

        sessionInfo.put("cookies", COOKIES);
        sessionInfo.put("timestamp", new Date());


        isLoginCookieValid(sevenMinutesAgo);


    }
}
