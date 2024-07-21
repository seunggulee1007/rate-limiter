package com.yeseung.ratelimiter.repository;

import org.redisson.api.RLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnMissingBean(value = LockRepository.class)
public class ConcurrentHashMapRepository extends LockRepository {

    private final ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<>();

    @Override
    public RLock lock(String key) {
        return (RLock)lockMap.computeIfAbsent(key, k -> new Object());
    }

}

