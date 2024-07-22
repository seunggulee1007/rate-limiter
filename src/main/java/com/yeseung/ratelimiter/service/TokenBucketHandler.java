package com.yeseung.ratelimiter.service;

import com.yeseung.ratelimiter.advice.exceptions.RateLimitException;
import com.yeseung.ratelimiter.domain.TokenInfo;
import com.yeseung.ratelimiter.properties.TokenBucketProperties;
import com.yeseung.ratelimiter.repository.RedisTokenBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "rate-type", havingValue = "token_bucket")
public class TokenBucketHandler implements RateLimitHandler {

    private final RedisTokenBucketRepository repository;
    private final TokenBucketProperties properties;

    @Override
    public boolean allowRequest(String key) {

        TokenInfo tokenInfo = repository.getOrDefault(key);
        refill(key, tokenInfo);
        if (!tokenInfo.isAllowRequest()) {
            throw new RateLimitException("You have reached the limit",
                                         tokenInfo.getRemaining(),
                                         tokenInfo.getLimit(),
                                         tokenInfo.getRetryAfter());
        }
        tokenInfo.minusTokens();
        repository.save(key, tokenInfo);
        return true;

    }

    private void refill(String key, TokenInfo tokenInfo) {
        long now = System.currentTimeMillis();
        long lastRefillTimestamp = tokenInfo.getLastRefillTimestamp();
        if (now > lastRefillTimestamp) {
            long elapsedTime = now - lastRefillTimestamp;
            int rate = properties.getRateUnit().toMillis();
            int tokensToAdd = (int)elapsedTime * rate / 1000;
            if (tokensToAdd > 0) {
                tokenInfo.calculateCurrentTokens(tokensToAdd);
                repository.save(key, tokenInfo);
            }
        }
    }

}
