package com.yeseung.ratelimiter.aop;

import com.yeseung.ratelimiter.annotations.RateLimiting;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RateLimitAop {

    @Around("@annotation(com.yeseung.ratelimiter.annotations.RateLimiting)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiting rateLimiting = method.getAnnotation(RateLimiting.class);
        if (rateLimiting == null) {
            // 어노테이션이 없는 경우 처리하지 않음
            return joinPoint.proceed();
        }
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        // SpEL 파서와 컨텍스트를 생성합니다.
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 메서드 파라미터를 컨텍스트에 추가합니다.
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // SpEL 표현식을 평가하여 값을 가져옵니다.
        String spelExpression = rateLimiting.cacheKey();
        Object cacheKeyValue = parser.parseExpression(spelExpression).getValue(context, Object.class);

        System.err.println(cacheKeyValue);

        return joinPoint.proceed();
    }

}
