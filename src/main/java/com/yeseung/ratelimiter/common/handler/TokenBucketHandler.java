package com.yeseung.ratelimiter.common.handler;

import com.yeseung.ratelimiter.common.domain.TokenInfo;
import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import com.yeseung.ratelimiter.common.ratelimit.RedisTokenBucketRedisTemplate;
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
    private final RedisTokenBucketRedisTemplate repository;
    private final TokenBucketProperties properties;

    @Override
    public TokenInfo allowRequest(String key) {

        TokenInfo tokenInfo = repository.getOrDefault(key);
        refill(key, tokenInfo);
        log.error("capacity :: {}, currentTokens :: {}, lastRefillTimestamp :: {}",
                  tokenInfo.getCapacity(),
                  tokenInfo.getCurrentTokens(),
                  tokenInfo.getLastRefillTimestamp());
        if (!tokenInfo.isAllowRequest()) {
            log.error("허용되지 않은 요청입니다.");
            throw new RateLimitException("You have reached the limit",
                                         tokenInfo.getRemaining(),
                                         tokenInfo.getLimit(),
                                         tokenInfo.getRetryAfter());
        }
        tokenInfo.minusTokens();
        repository.save(key, tokenInfo);
        return tokenInfo;

    }

    private void refill(String key, TokenInfo tokenInfo) {
        long now = System.currentTimeMillis();
        long lastRefillTimestamp = tokenInfo.getLastRefillTimestamp();
        if (now > lastRefillTimestamp) {
            long elapsedTime = now - lastRefillTimestamp;
            int rate = properties.getRateUnit().toMillis();
            int tokensToAdd = (int)elapsedTime / rate;
            if (tokensToAdd > 0) {
                tokenInfo.calculateCurrentTokens(tokensToAdd);
                repository.save(key, tokenInfo);
            }
        }
    }

}
