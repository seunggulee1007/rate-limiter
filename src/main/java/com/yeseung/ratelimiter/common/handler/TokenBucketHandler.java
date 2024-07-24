package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.cache.CacheTemplate;
import com.yeseung.ratelimiter.common.domain.TokenBucketInfo;
import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "rate-type", havingValue = "token_bucket")
public class TokenBucketHandler implements RateLimitHandler {

    private static final Logger log = LoggerFactory.getLogger(TokenBucketHandler.class);
    private final CacheTemplate cacheTemplate;
    private final TokenBucketProperties properties;

    @Override
    public TokenBucketInfo allowRequest(String key) {

        TokenBucketInfo tokenBucketInfo = (TokenBucketInfo)cacheTemplate.getOrDefault(key, TokenBucketInfo.class);
        refill(key, tokenBucketInfo);
        log.error("capacity :: {}, currentTokens :: {}, lastRefillTimestamp :: {}",
                  tokenBucketInfo.getCapacity(),
                  tokenBucketInfo.getCurrentTokens(),
                  tokenBucketInfo.getLastRefillTimestamp());
        if (!tokenBucketInfo.isAllowRequest()) {
            log.error("허용되지 않은 요청입니다.");
            throw new RateLimitException("You have reached the limit",
                                         tokenBucketInfo.getRemaining(),
                                         tokenBucketInfo.getLimit(),
                                         tokenBucketInfo.getRetryAfter());
        }
        tokenBucketInfo.minusTokens();
        cacheTemplate.save(key, tokenBucketInfo);
        return tokenBucketInfo;

    }

    private void refill(String key, TokenBucketInfo tokenBucketInfo) {
        long now = System.currentTimeMillis();
        long lastRefillTimestamp = tokenBucketInfo.getLastRefillTimestamp();
        if (now > lastRefillTimestamp) {
            long elapsedTime = now - lastRefillTimestamp;
            int rate = properties.getRateUnit().toMillis();
            int tokensToAdd = (int)elapsedTime / rate;
            if (tokensToAdd > 0) {
                tokenBucketInfo.calculateCurrentTokens(tokensToAdd);
                cacheTemplate.save(key, tokenBucketInfo);
            }
        }
    }

}
