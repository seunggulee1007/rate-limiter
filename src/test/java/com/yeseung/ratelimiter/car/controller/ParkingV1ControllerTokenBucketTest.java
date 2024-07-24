package com.yeseung.ratelimiter.car.controller;

import com.yeseung.ratelimiter.car.controller.request.ParkingApplyRequest;
import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import com.yeseung.ratelimiter.container.RedisTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "rate-limiter.enabled=true",
    "rate-limiter.lock-type=redis_redisson",
    "rate-limiter.rate-type=token_bucket",
    "rate-limiter.cache-type=REDIS",
})
@AutoConfigureMockMvc
class ParkingV1ControllerTokenBucketTest extends RedisTestContainer {

    private static final Logger log = LoggerFactory.getLogger(ParkingV1ControllerTokenBucketTest.class);

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private TokenBucketProperties tokenBucketProperties;

    @Test
    @DisplayName("주차권 신청 처리율 제한 테스트")
    void test() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // given
        ParkingApplyRequest carInfo = new ParkingApplyRequest("07로3725", "seunggulee", "20240722", "10", "00");
        String s = objectMapper.writeValueAsString(carInfo);
        int capacity = tokenBucketProperties.getCapacity();
        int count = capacity + 2;
        for (int i = 0; i < count; i++) {
            confirm_remaining(s, capacity - i - 1);
        }

    }

    private void confirm_remaining(String parkingInfo, int count) throws Exception {
        if (count >= 0) {
            mockMvc.perform(post("/api/v1/car/parking").contentType(MediaType.APPLICATION_JSON).content(parkingInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-RateLimit-Remaining", String.valueOf(count)))
            ;
        } else {

            mockMvc.perform(post("/api/v1/car/parking").contentType(MediaType.APPLICATION_JSON).content(parkingInfo))
                .andDo(print())
                .andExpect(status().isTooManyRequests())
                .andExpect(result -> assertInstanceOf(RateLimitException.class,
                                                      result.getResolvedException()));
        }

    }

}