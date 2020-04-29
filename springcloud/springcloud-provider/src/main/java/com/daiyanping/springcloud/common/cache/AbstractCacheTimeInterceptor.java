package com.daiyanping.springcloud.common.cache;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

/**
 * 一级缓存，二级缓存，缓存时间同步支持
 * @author daiyanping
 */
@Data
public abstract class AbstractCacheTimeInterceptor {

    private final static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Autowired
    private RedisTemplate redisTemplate;


    private static long getCacheTime() {
        return threadLocal.get();
    }

    private static void setCacheTime(Long cacheTime) {
        threadLocal.set(cacheTime);
    }

    private static void remove() {
        threadLocal.remove();
    }

    /**
     * 根据key同步一级缓存时间
     * @param cacheName
     * @param key
     */
    protected void syncCacheTimeForTwoCache(String cacheName, String key) {
        Long expire = redisTemplate.boundValueOps(cacheName + "::" + key).getExpire();
        setCacheTime(expire);
    }

    /**
     * 根据TTL同步一级缓存时间
     * @param ttl
     */
    protected void syncCacheTimeForTTL(Duration ttl) {
        setCacheTime(ttl.toMillis());
    }

    /**
     * 清除CacheTimeThreadLocal
     */
    protected void cleanThreadLocal() {
        remove();
    }

    protected long getFirstCacheTime() {
        return getCacheTime();
    }

}
