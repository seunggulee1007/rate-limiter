package com.yeseung.ratelimiter.car.repository;

import com.yeseung.ratelimiter.car.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingRepository extends JpaRepository<CarEntity, Long> {

    List<CarEntity> findAllByCarNoIs(String carNo);

}
