package com.yeseung.ratelimiter.service;

import com.yeseung.ratelimiter.annotations.RateLimiting;
import com.yeseung.ratelimiter.domain.CarInfo;
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
