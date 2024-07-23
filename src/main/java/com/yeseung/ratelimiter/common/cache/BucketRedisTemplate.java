package com.yeseung.ratelimiter.common.cache;

import com.yeseung.ratelimiter.common.domain.TokenInfo;
import com.yeseung.ratelimiter.common.properties.TokenBucketProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "cache-type", havingValue = "redis")
public class BucketRedisTemplate implements CacheTemplate {

    private final RedisTemplate<String, TokenInfo> redisTokenInfoTemplate;
    private final TokenBucketProperties tokenBucketProperties;

    @Override
    public TokenInfo getOrDefault(final String key) {
        return Optional.ofNullable(redisTokenInfoTemplate.opsForValue().get(key)).orElseGet(() -> TokenInfo.create(
            tokenBucketProperties));
    }

    @Override
    public void save(String key, TokenInfo tokenInfo) {
        redisTokenInfoTemplate.opsForValue().set(key, tokenInfo, Duration.ofMillis(3_000));
    }

}
