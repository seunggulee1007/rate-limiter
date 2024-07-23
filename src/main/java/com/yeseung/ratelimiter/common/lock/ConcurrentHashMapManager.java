package com.yeseung.ratelimiter.common.lock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository
@ConditionalOnProperty(prefix = "rate-limiter", value = "lock-type", havingValue = "concurrent_hash_map")
public class ConcurrentHashMapManager extends LockManager {

    private final ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    @Override
    public void getLock(String key) {
        this.lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
    }

}

