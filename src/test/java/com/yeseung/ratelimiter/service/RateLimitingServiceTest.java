package com.yeseung.ratelimiter.service;

import com.yeseung.ratelimiter.domain.CarInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RateLimitingServiceTest {

    @Autowired
    private RateLimitingService rateLimitingService;

    @Test
    @DisplayName("어노테이션 테스트")
    void testRateLimitingAnnotation() {
        // given
        CarInfo carInfo = new CarInfo("1234", "KIA", "1234");
        // when
        for (int i = 0; i < 100; i++) {
            rateLimitingService.rateLimitingService(carInfo);
        }
        // then

    }

}