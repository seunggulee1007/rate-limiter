package com.yeseung.ratelimiter.common.properties;

import com.yeseung.ratelimiter.common.enums.LockType;
import com.yeseung.ratelimiter.common.enums.RateType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("rate-limiter")
public class RateLimitingProperties {

    private boolean enabled;

    private LockType lockType;

    private RateType rateType;

    private CacheType cacheType;

}
