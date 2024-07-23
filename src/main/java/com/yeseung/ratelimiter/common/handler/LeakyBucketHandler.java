package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.domain.TokenInfo;

public class LeakyBucketHandler implements RateLimitHandler {

    @Override
    public TokenInfo allowRequest(String key) {
        return null;
    }

}
