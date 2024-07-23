package com.yeseung.ratelimiter.common.lock;

import com.yeseung.ratelimiter.common.annotations.RateLimiting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "lock-type", havingValue = "redis_redisson")
public class RedisRedissonManager extends LockManager {

    private final RedissonClient redissonClient;

    private RLock rLock;

    @Override
    public void getLock(String key) {
        this.rLock = redissonClient.getLock(key);
    }

    @Override
    public boolean tryLock(RateLimiting rateLimiting) throws InterruptedException {
        return rLock.tryLock(rateLimiting.waitTime(), rateLimiting.leaseTime(), rateLimiting.timeUnit());
    }

    @Override
    public void unlock() {
        if (rLock != null && rLock.isLocked() && rLock.isHeldByCurrentThread()) {
            rLock.unlock();
        }
    }

}
