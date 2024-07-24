package com.yeseung.ratelimiter.common.domain;

import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbstractTokenInfo {

    protected int capacity;
    protected long lastRefillTimestamp;
    protected int currentTokens;
    protected int rate;

    public AbstractTokenInfo(TokenBucketProperties tokenBucketProperties) {
        this.capacity = tokenBucketProperties.getCapacity();
        this.currentTokens = tokenBucketProperties.getCapacity();
        this.lastRefillTimestamp = System.currentTimeMillis();
        this.rate = tokenBucketProperties.getRateUnit().toMillis();
    }

    public int getRemaining() {
        return this.currentTokens;
    }

    public int getLimit() {
        return this.capacity;
    }

    public int getRetryAfter() {
        return (int)(System.currentTimeMillis() - this.lastRefillTimestamp) / this.rate;
    }

    public void endProcess() {

    }

}
