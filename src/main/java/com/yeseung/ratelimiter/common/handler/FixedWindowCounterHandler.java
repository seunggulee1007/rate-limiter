package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.cache.CacheTemplate;
import com.yeseung.ratelimiter.common.domain.AbstractTokenInfo;
import com.yeseung.ratelimiter.common.domain.FixedWindowCountInfo;
import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FixedWindowCounterHandler implements RateLimitHandler {

    private final CacheTemplate cacheTemplate;

    @Override
    public AbstractTokenInfo allowRequest(String key) {
        FixedWindowCountInfo fixedWindowCounterInfo = (FixedWindowCountInfo)cacheTemplate.getOrDefault(key, FixedWindowCountInfo.class);
        if (!fixedWindowCounterInfo.isAvailable()) {
            log.error("허용되지 않은 요청입니다.");
            throw new RateLimitException("You have reached the limit",
                                         fixedWindowCounterInfo.getRemaining(),
                                         fixedWindowCounterInfo.getLimit(),
                                         fixedWindowCounterInfo.getRetryAfter());
        }
        fixedWindowCounterInfo.plusCount();
        cacheTemplate.save(key, fixedWindowCounterInfo);
        return fixedWindowCounterInfo;
    }

}
