package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.cache.CacheTemplate;
import com.yeseung.ratelimiter.common.domain.AbstractTokenInfo;
import com.yeseung.ratelimiter.common.domain.LeakyBucketInfo;
import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "rate-type", havingValue = "leaky_bucket")
public class LeakyBucketHandler implements RateLimitHandler {

    private final CacheTemplate cacheTemplate;

    @Override
    public AbstractTokenInfo allowRequest(String key) {
        LeakyBucketInfo bucketInfo = (LeakyBucketInfo)cacheTemplate.getOrDefault(key, LeakyBucketInfo.class);
        if (bucketInfo.allowRequest()) {
            cacheTemplate.save(key, bucketInfo);
            return bucketInfo;
        }
        throw new RateLimitException("You have reached the limit",
                                     bucketInfo.getRemaining(),
                                     bucketInfo.getLimit(),
                                     bucketInfo.getRetryAfter());
    }

}
