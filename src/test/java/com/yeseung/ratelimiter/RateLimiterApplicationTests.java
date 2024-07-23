package com.yeseung.ratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestConfiguration(proxyBeanMethods = false)
class RateLimiterApplicationTests {

    public static void main(String[] args) {
        SpringApplication.from(RateLimiterApplication::main).with(RateLimiterApplicationTests.class).run(args);
    }

}
