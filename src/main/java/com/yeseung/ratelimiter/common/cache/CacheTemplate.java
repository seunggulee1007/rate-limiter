package com.yeseung.ratelimiter.common.cache;

import com.yeseung.ratelimiter.common.domain.AbstractTokenInfo;

public interface CacheTemplate {

    AbstractTokenInfo getOrDefault(final String key, Class<? extends AbstractTokenInfo> tokenBucketInfoClass);

    void save(String key, AbstractTokenInfo tokenInfo);

}
