package com.yeseung.ratelimiter.common.properties;

import com.yeseung.ratelimiter.common.enums.RateUnit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties("token-bucket")
public class TokenBucketProperties {

    private int capacity;
    private int rate;
    private RateUnit rateUnit;

}
