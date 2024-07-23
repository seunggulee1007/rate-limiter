package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.domain.TokenInfo;

public interface RateLimitHandler {

    TokenInfo allowRequest(String key);

}
