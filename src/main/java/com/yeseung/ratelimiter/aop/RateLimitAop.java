package com.yeseung.ratelimiter.aop;

import com.yeseung.ratelimiter.annotations.RateLimiting;
import com.yeseung.ratelimiter.properties.LateLimitingProperties;
import com.yeseung.ratelimiter.repository.LockRepository;
import com.yeseung.ratelimiter.service.RateLimitHandler;
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
    private final LockRepository lockRepository;
    private final RateLimitHandler rateLimitHandler;

    @Around("@annotation(com.yeseung.ratelimiter.annotations.RateLimiting)")
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

        lockRepository.getLock(lockKey);
        try {
            log.error("{} lock 시작", this.getClass().getName());
            boolean lockable = lockRepository.tryLock(rateLimiting);
            if (!lockable) {
                log.error("Lock 획득 실패={}", lockKey);
                throw new RuntimeException("Lock 획득 실패했습니다.");
            }
            String cacheKey = "cache-".concat(lockKey);
            rateLimitHandler.allowRequest(cacheKey);
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            log.info("에러 발생 : {}", e.getMessage());
            throw e;
        } finally {
            log.error("{} lock 해제", this.getClass().getName());
            lockRepository.unlock();
        }
    }

}
