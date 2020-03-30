package com.daiyanping.cms.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.cache.caffeine.CaffeineCache;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 本地一级缓存
 */
public class LocalFirstLevelCacheInterceptor extends AbstractCacheInterceptor {

    private Cache<String, Object> cache;

    public LocalFirstLevelCacheInterceptor() {
        this.cache = Caffeine.newBuilder()
                // 自定义过期策略
                .expireAfter(new MyExpiry())
                .maximumSize(3)
                .build();
    }

    @Override
    protected boolean matchMethod(Method method) {
        return "get".equals(method.getName());
    }

    @Override
    protected Object invokeMethod(MethodInvocation invocation) throws Throwable {
        Object[] objects = invocation.getArguments();
        String key = (String) objects[0];
        return cache.get(key, key2 -> {
            try {
                return invocation.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("缓存获取异常");
            }
        });
    }

    private static class MyExpiry implements Expiry {

        @Override
        public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
            return currentTime + 1000 * 60  * 10;
        }

        @Override
        public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }

        // 直接返回currentDuration表示不启用读过期设置
        @Override
        public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }
    }
}
