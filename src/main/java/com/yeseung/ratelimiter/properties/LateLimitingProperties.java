package com.yeseung.ratelimiter.properties;

import com.yeseung.ratelimiter.enums.CacheToUse;
import com.yeseung.ratelimiter.enums.RateType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("rate-limiter")
public class LateLimitingProperties {

    private boolean enabled;

    private CacheToUse cacheToUse;

    private RateType rateType;

}
