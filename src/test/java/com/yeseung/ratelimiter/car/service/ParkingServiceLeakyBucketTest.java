package com.yeseung.ratelimiter.car.service;

import com.yeseung.ratelimiter.car.controller.request.ParkingApplyRequest;
import com.yeseung.ratelimiter.car.entity.CarEntity;
import com.yeseung.ratelimiter.car.repository.ParkingRepository;
import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import com.yeseung.ratelimiter.container.RedisTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "rate-limiter.enabled=true",
    "rate-limiter.lock-type=redis_redisson",
    "rate-limiter.rate-type=leaky_bucket",
    "rate-limiter.cache-type=REDIS",
})
class ParkingServiceLeakyBucketTest extends RedisTestContainer {

    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private TokenBucketProperties tokenBucketProperties;

    @BeforeEach
    public void beforeEach() {
        parkingRepository.deleteAll();
    }

    @Test
    @DisplayName("주차권 저장 테스트")
    void lateLimitingTest() throws Exception {
        // given
        String carNo = "07로3725";
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        ParkingApplyRequest parkingApplyRequest = new ParkingApplyRequest("seunggulee", carNo, "20240722", "10", "00");
                        parkingService.parking(parkingApplyRequest);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }
        // then
        List<CarEntity> allByCarNoIs = parkingRepository.findAllByCarNoIs(carNo);

        assertThat(allByCarNoIs).hasSize(tokenBucketProperties.getCapacity());

    }

}