package com.yeseung.ratelimiter.common.domain;

import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;

import java.util.ArrayDeque;
import java.util.Deque;

public class LeakyBucketInfo extends AbstractTokenInfo {

    private final Deque<String> deque;

    public LeakyBucketInfo(TokenBucketProperties tokenBucketProperties) {
        super(tokenBucketProperties);
        deque = new ArrayDeque<>(tokenBucketProperties.getCapacity());
        capacity = tokenBucketProperties.getCapacity();
    }

    public boolean allowRequest() {
        if (deque.size() == capacity) {
            return false;
        }
        deque.addFirst(String.valueOf(System.currentTimeMillis()));
        return true;
    }

    @Override
    public int getLimit() {
        return deque.size();
    }

    @Override
    public int getRemaining() {
        return capacity - deque.size();
    }

    @Override
    public void endProcess() {
        if (!deque.isEmpty()) {
            deque.removeLast();
        }
    }

}
