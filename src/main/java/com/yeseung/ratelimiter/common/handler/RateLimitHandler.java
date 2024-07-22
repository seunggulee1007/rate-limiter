package com.yeseung.ratelimiter.common.handler;

public interface RateLimitHandler {

    boolean allowRequest(String key);

}
