package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.cache.CacheTemplate;
import com.yeseung.ratelimiter.common.domain.FixedWindowCountInfo;
import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "rate-type", havingValue = "fixed_window_counter")
public class FixedWindowCounterHandler implements RateLimitHandler {

    private final CacheTemplate cacheTemplate;

    @Override
    public FixedWindowCountInfo allowRequest(String key) {
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
