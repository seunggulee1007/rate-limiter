package com.yeseung.ratelimiter.common.aop;

import com.yeseung.ratelimiter.common.annotations.RateLimiting;
import com.yeseung.ratelimiter.common.lock.LockManager;
import com.yeseung.ratelimiter.common.properties.LateLimitingProperties;
import com.yeseung.ratelimiter.common.handler.RateLimitHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAop {

    private final LateLimitingProperties lateLimitingProperties;
    private final LockManager lockManager;
    private final RateLimitHandler rateLimitHandler;

    @Around("@annotation(com.yeseung.ratelimiter.common.annotations.RateLimiting)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!lateLimitingProperties.isEnabled()) {
            return joinPoint.proceed();
        }
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiting rateLimiting = method.getAnnotation(RateLimiting.class);
        if (rateLimiting == null) {
            // 어노테이션이 없는 경우 처리하지 않음
            return joinPoint.proceed();
        }
        String lockKey =
            method.getName() + CustomSpringELParser.getDynamicValue(signature.getParameterNames(),
                                                                    joinPoint.getArgs(),
                                                                    rateLimiting.cacheKey());

        lockManager.getLock(lockKey);

        try {
            boolean lockable = lockManager.tryLock(rateLimiting);
            if (!lockable) {
                log.error("Lock 획득 실패={}", lockKey);
                throw new RuntimeException("Lock 획득 실패했습니다.");
            }
            log.error("{} lock 시작", this.getClass().getName());
            String cacheKey = "cache-".concat(lockKey);
            rateLimitHandler.allowRequest(cacheKey);
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            log.info("에러 발생 : {}", e.getMessage());
            throw e;
        } finally {
            log.error("{} lock 해제", this.getClass().getName());
            lockManager.unlock();
        }
    }

}
