package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.cache.CacheTemplate;
import com.yeseung.ratelimiter.common.domain.AbstractTokenInfo;
import com.yeseung.ratelimiter.common.domain.LeakyBucketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "rate-type", havingValue = "leaky-bucket")
public class LeakyBucketHandler implements RateLimitHandler {

    private final CacheTemplate cacheTemplate;

    @Override
    public AbstractTokenInfo allowRequest(String key) {
        LeakyBucketInfo bucketInfo = (LeakyBucketInfo)cacheTemplate.getOrDefault(key, LeakyBucketInfo.class);
        return null;
    }

}
