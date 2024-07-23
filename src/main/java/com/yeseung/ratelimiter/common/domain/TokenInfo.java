package com.yeseung.ratelimiter.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInfo implements Serializable {

    private int capacity;
    private long lastRefillTimestamp;
    private int currentTokens;
    private int rate;

    public static TokenInfo create(TokenBucketProperties properties) {

        return new TokenInfo(properties.getCapacity(),
                             System.currentTimeMillis(),
                             properties.getCapacity(),
                             properties.getRateUnit().toMillis());
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

    public int getRemaining() {
        return this.currentTokens;
    }

    public int getLimit() {
        return this.capacity;
    }

    public int getRetryAfter() {
        return (int)(System.currentTimeMillis() - this.lastRefillTimestamp) / this.rate;
    }

}
