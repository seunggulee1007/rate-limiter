package com.yeseung.ratelimiter.common.domain;

import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;

import java.util.ArrayDeque;
import java.util.Deque;

public class LeakyBucketInfo extends AbstractTokenInfo {

    private final Deque<String> deque;

    public LeakyBucketInfo(TokenBucketProperties tokenBucketProperties) {
        super(tokenBucketProperties);
        deque = new ArrayDeque<>(tokenBucketProperties.getCapacity());
    }

}
