package com.yeseung.ratelimiter.repository;

import org.redisson.api.RLock;

public abstract class LockRepository {

    private RLock lock;

    public abstract RLock lock(final String key);

    public void unlock() {
        if (this.lock != null && this.lock.isLocked() && this.lock.isHeldByCurrentThread()) {
            this.lock.unlock();
        }
    }

}
