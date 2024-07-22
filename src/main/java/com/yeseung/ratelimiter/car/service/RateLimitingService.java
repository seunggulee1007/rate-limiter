package com.yeseung.ratelimiter.car.service;

import com.yeseung.ratelimiter.car.domain.CarInfo;
import com.yeseung.ratelimiter.car.entity.CarEntity;
import com.yeseung.ratelimiter.car.repository.ParkingRepository;
import com.yeseung.ratelimiter.common.annotations.RateLimiting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

    private final ParkingRepository parkingRepository;

    @RateLimiting(
        name = "rate-limiting-service",
        cacheKey = "#carInfo.carNo"
    )
    public void rateLimitingService(CarInfo carInfo) {
        parkingRepository.save(CarEntity.from(carInfo));
    }

}
