package com.daiyanping.cms.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName RedisCacheManagerProxy
 * @Description TODO RedisCacheManager代理类，用于创建RedisCacheProxy
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@Data
@AllArgsConstructor
public class RedisCacheManagerProxy implements MethodInterceptor {

    private RedisCacheManager redisCacheManager;

    private RedissonClient redissonClient;

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().equals("getCache")) {

            Cache cache = this.cacheMap.get(objects[0]);
            if (cache != null) {
                return cache;
            }
            else {
                synchronized (this.cacheMap) {
                    Cache redisCache = (Cache) method.invoke(redisCacheManager, objects);
                    RedisCacheProxy redisCacheProxy = new RedisCacheProxy(redisCache, redissonClient, redisCache.getName(), threadPoolTaskScheduler);
                    Enhancer enhancer = new Enhancer();
                    if (redisCache.getClass().isAssignableFrom(TransactionAwareCacheDecorator.class)) {

                       enhancer.setSuperclass(TransactionAwareCacheDecorator.class);
                    }
                    if (redisCache.getClass().isAssignableFrom(RedisCache.class)) {
                        enhancer.setSuperclass(RedisCache.class);
                    }
                    enhancer.setInterfaces(new Class<?>[] {Cache.class});
                    enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
                    enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class});
                    enhancer.setCallback(redisCacheProxy);
                    Cache newCache = null;
                    if (redisCache.getClass().isAssignableFrom(TransactionAwareCacheDecorator.class)) {
                        newCache = (Cache) enhancer.create(new Class[]{Cache.class}, new Object[]{redisCache});
                    }
                    if (redisCache.getClass().isAssignableFrom(RedisCache.class)) {

                        newCache = (Cache) enhancer.create(new Class[]{String.class, RedisCacheWriter.class, RedisCacheConfiguration.class}, new Object[]{"", new MyRedisCacheWriter(), RedisCacheConfiguration.defaultCacheConfig()});
                    }
                    cacheMap.put(objects[0].toString(), newCache);
                    return newCache;
                }

            }
        }

        return method.invoke(redisCacheManager, objects);

    }
}
