package com.yeseung.ratelimiter.common.properties;

import com.yeseung.ratelimiter.common.enums.CacheToUse;
import com.yeseung.ratelimiter.common.enums.RateType;
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
