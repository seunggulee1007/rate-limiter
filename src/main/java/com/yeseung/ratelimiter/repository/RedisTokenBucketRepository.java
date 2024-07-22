package com.yeseung.ratelimiter.repository;

import com.yeseung.ratelimiter.domain.TokenInfo;
import com.yeseung.ratelimiter.properties.TokenBucketProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisTokenBucketRepository {

    private final RedisTemplate<String, TokenInfo> redisTokenInfoTemplate;
    private final TokenBucketProperties tokenBucketProperties;

    public TokenInfo getOrDefault(final String key) {
        return Optional.ofNullable(redisTokenInfoTemplate.opsForValue().get(generateKey(key))).orElseGet(() -> TokenInfo.create(
            tokenBucketProperties));
    }

    private String generateKey(final String key) {
        return key;
    }

    public void save(String key, TokenInfo tokenInfo) {
        redisTokenInfoTemplate.opsForValue().set(generateKey(key), tokenInfo, Duration.ofMillis(3_000));
    }

}
