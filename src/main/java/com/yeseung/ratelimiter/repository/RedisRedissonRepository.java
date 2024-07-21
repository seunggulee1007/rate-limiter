package com.yeseung.ratelimiter.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "cache-to-use", havingValue = "REDIS_REDISSON")
public class RedisRedissonRepository extends LockRepository {

    private final RedissonClient redissonClient;

    @Override
    public RLock lock(String key) {
        return redissonClient.getLock(key);
    }

}
