package com.yeseung.ratelimiter.common.cache;

import com.yeseung.ratelimiter.common.domain.AbstractTokenInfo;
import com.yeseung.ratelimiter.common.properties.BucketProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rate-limiter", value = "cache-type", havingValue = "redis")
public class BucketRedisTemplate implements CacheTemplate {

    private final RedisTemplate<String, AbstractTokenInfo> redisTokenInfoTemplate;
    private final BucketProperties bucketProperties;

    @Override
    public AbstractTokenInfo getOrDefault(final String key, Class<? extends AbstractTokenInfo> clazz) {
        return Optional.ofNullable(redisTokenInfoTemplate.opsForValue().get(key))
            .orElseGet(() -> {
                try {
                    return clazz.getDeclaredConstructor(BucketProperties.class).newInstance(bucketProperties);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public void save(String key, AbstractTokenInfo tokenInfo) {
        redisTokenInfoTemplate.opsForValue().set(key, tokenInfo, Duration.ofMillis(3_000));
    }

}
