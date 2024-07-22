package com.yeseung.ratelimiter.common.ratelimit;

import com.yeseung.ratelimiter.common.domain.TokenInfo;
import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisTokenBucketRedisTemplate {

    private static final Logger log = LoggerFactory.getLogger(RedisTokenBucketRedisTemplate.class);
    private final RedisTemplate<String, TokenInfo> redisTokenInfoTemplate;
    private final TokenBucketProperties tokenBucketProperties;

    public TokenInfo getOrDefault(final String key) {
        return Optional.ofNullable(redisTokenInfoTemplate.opsForValue().get(key)).orElseGet(() -> TokenInfo.create(
            tokenBucketProperties));
    }

    public void save(String key, TokenInfo tokenInfo) {
        redisTokenInfoTemplate.opsForValue().set(key, tokenInfo, Duration.ofMillis(3_000));
    }

}
