package com.yeseung.ratelimiter.service;

import com.yeseung.ratelimiter.car.service.RateLimitingService;
import com.yeseung.ratelimiter.container.RedisTestContainer;
import com.yeseung.ratelimiter.car.domain.CarInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class RateLimitingServiceTest extends RedisTestContainer {

    @Autowired
    private RateLimitingService rateLimitingService;

    @Test
    @DisplayName("어노테이션 테스트")
    void testRateLimitingAnnotation() throws InterruptedException {
        // given
        CarInfo carInfo = new CarInfo("1234", "KIA", "1234");
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    rateLimitingService.rateLimitingService(carInfo);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // then

    }

}