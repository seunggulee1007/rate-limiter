package com.yeseung.ratelimiter.car.service;

import com.yeseung.ratelimiter.car.domain.CarInfo;
import com.yeseung.ratelimiter.common.annotations.RateLimiting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

    @RateLimiting(
        name = "rate-limiting-service",
        cacheKey = "#carInfo.carNo"
    )
    public void rateLimitingService(CarInfo carInfo) {
        System.out.println("Rate limiting service called with car info: " + carInfo);
    }

}
