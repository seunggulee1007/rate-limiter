package com.yeseung.ratelimiter.common.cache;

import com.yeseung.ratelimiter.common.domain.TokenInfo;

public interface CacheTemplate {

    TokenInfo getOrDefault(final String key);

    void save(String key, TokenInfo tokenInfo);

}
