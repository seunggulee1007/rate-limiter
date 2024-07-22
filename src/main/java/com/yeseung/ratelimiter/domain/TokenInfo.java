package com.yeseung.ratelimiter.domain;

import com.yeseung.ratelimiter.properties.TokenBucketProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInfo {

    private int capacity;
    private long lastRefillTimestamp;
    private int currentTokens;

    public static TokenInfo create(TokenBucketProperties properties) {
        return new TokenInfo(properties.getCapacity(), System.currentTimeMillis(), properties.getCapacity());
    }

    public boolean isAllowRequest() {
        return this.currentTokens > 0;
    }

    public void minusTokens() {
        this.currentTokens--;
    }

    public void calculateCurrentTokens(int tokensToAdd) {
        this.currentTokens = Math.min(this.currentTokens + tokensToAdd, this.capacity);
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

}
