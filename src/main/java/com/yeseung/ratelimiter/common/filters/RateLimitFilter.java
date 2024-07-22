package com.yeseung.ratelimiter.common.filters;

import com.yeseung.ratelimiter.common.exceptions.RateLimitException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
        ServletException,
        IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RateLimitException e) {
            response.setIntHeader("X-Ratelimit-Remaining", e.getRemaining());
            response.setIntHeader("X-Ratelimit-Limit", e.getLimit());
            response.setIntHeader("X-Ratelimit-Retry-After", e.getRetryAfter());
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
    }

}
