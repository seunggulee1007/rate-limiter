package com.yeseung.ratelimiter.car.service;

import com.yeseung.ratelimiter.car.controller.request.ParkingApplyRequest;
import com.yeseung.ratelimiter.car.controller.response.ParkingApplyResponse;
import com.yeseung.ratelimiter.car.entity.CarEntity;
import com.yeseung.ratelimiter.car.repository.ParkingRepository;
import com.yeseung.ratelimiter.common.annotations.RateLimiting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;

    @RateLimiting(
        name = "rate-limiting-service",
        cacheKey = "#request.carNo"
    )
    public ParkingApplyResponse parking(ParkingApplyRequest request) {
        CarEntity savedEntity = parkingRepository.save(CarEntity.from(request));
        return ParkingApplyResponse.from(savedEntity);
    }

}