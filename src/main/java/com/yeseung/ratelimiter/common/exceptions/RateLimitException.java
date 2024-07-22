package com.yeseung.ratelimiter.common.exceptions;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {

    private final int remaining;
    private final int limit;
    private final int retryAfter;

    public RateLimitException(String message, int remaining, int limit, int retryAfter) {
        super(message);
        this.remaining = remaining;
        this.limit = limit;
        this.retryAfter = retryAfter;
    }

}
