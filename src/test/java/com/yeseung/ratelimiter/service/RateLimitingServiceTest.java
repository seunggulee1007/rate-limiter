package com.yeseung.ratelimiter.service;

import com.yeseung.ratelimiter.domain.CarInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RateLimitingServiceTest {

    @Autowired
    private RateLimitingService rateLimitingService;

    @Test
    @DisplayName("어노테이션 테스트")
    void testRateLimitingAnnotation () {
        // given
        CarInfo carInfo = new CarInfo("1234", "KIA","1234");
        // when
        rateLimitingService.rateLimitingService(carInfo);
        // then

    }
}