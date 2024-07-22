package com.yeseung.ratelimiter.service;

public interface RateLimitHandler {

    boolean allowRequest(String key);
    
}
