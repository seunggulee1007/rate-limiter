package com.yeseung.ratelimiter.common.advice;

import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import com.yeseung.ratelimiter.common.util.ApiUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiUtil.ApiResponse<Void> handleRateLimitException(RateLimitException e) {
        HttpServletResponse response = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getResponse();
        if (response != null) {
            response.setIntHeader("X-Ratelimit-Remaining", e.getRemaining());
            response.setIntHeader("X-Ratelimit-Limit", e.getLimit());
            response.setIntHeader("X-Ratelimit-Retry-After", e.getRetryAfter());
        }
        return ApiUtil.fail(e.getMessage());
    }

}
