package com.yeseung.ratelimiter.repository;

import com.yeseung.ratelimiter.annotations.RateLimiting;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;

@Slf4j
public abstract class LockRepository {

    protected Lock lock;

    public boolean tryLock(RateLimiting rateLimiting) throws InterruptedException {
        return lock.tryLock(rateLimiting.waitTime(), rateLimiting.timeUnit());
    }

    public abstract void getLock(String key);

    public void unlock() {
        lock.unlock();
    }

}
