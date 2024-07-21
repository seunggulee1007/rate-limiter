package com.yeseung.ratelimiter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiting {

    String name();

    String cacheKey() default "";

    String executeCondition() default "";

    String skipCondition() default "";

    boolean ratePerMethod() default false;

    String fallbackMethodName() default "";

}
