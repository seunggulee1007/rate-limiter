package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.domain.AbstractTokenInfo;

public interface RateLimitHandler {

    AbstractTokenInfo allowRequest(String key);

}
