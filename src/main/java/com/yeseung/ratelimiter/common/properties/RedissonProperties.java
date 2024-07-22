package com.yeseung.ratelimiter.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties("redisson")
public class RedissonProperties {

    private String address;

}
