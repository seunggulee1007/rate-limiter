package com.yeseung.ratelimiter.common.domain;

import com.yeseung.ratelimiter.common.properties.BucketProperties;
import lombok.Getter;

@Getter
public class FixedWindowCountInfo extends AbstractTokenInfo {

    private final int windowSize;
    private final int requestLimit;
    private int currentCount;

    public FixedWindowCountInfo(BucketProperties bucketProperties) {
        this.windowSize = bucketProperties.getFixedWindowCounter().getWindowSize();
        this.requestLimit = bucketProperties.getFixedWindowCounter().getRequestLimit();
        this.currentCount = 0;
    }

    @Override
    public int getRemaining() {
        return this.requestLimit - this.currentCount;
    }

    @Override
    public int getLimit() {
        return this.requestLimit;
    }

    @Override
    public int getRetryAfter() {
        return this.windowSize;
    }

    public boolean isAvailable() {
        return this.currentCount < this.requestLimit;
    }

    public void plusCount() {
        this.currentCount++;
    }

}
