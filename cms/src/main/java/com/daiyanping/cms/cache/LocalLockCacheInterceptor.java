package com.daiyanping.cms.cache;

import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.Cache;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存本地锁支持
 * @author daiyanping
 */
@Data
public class LocalLockCacheInterceptor extends AbstractCacheLockInterceptor {

    private Cache cache;

    // 定时清理本地锁,防止内存溢出
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private static String lockSuffix = "~lock";

    private final Map<String, Object> lockMap = new ConcurrentHashMap();

    public LocalLockCacheInterceptor(Cache cache, ThreadPoolTaskScheduler threadPoolTaskScheduler){
        this.cache = cache;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        /**
         * 清除本地锁,防止潜在内存泄漏风险,锁不多的情况下暂不使用,后续考虑使用
         */
        threadPoolTaskScheduler.schedule(() -> {
            lockMap.clear();
        }, new CronTrigger("0 0 4 * * ?"));
    }

    /**
     * 重新实现同步的get方法
     * @param key
     * @param valueLoader
     * @param <T>
     * @return
     */
    public <T> T get(Object key, Callable<T> valueLoader, MethodInvocation invocation) throws Throwable {

        // 获取到结果立马返回
        Cache.ValueWrapper result = cache.get(key);
        if (result != null) {
            return (T) result.get();
        }

        String lock = key + lockSuffix;

        Object o = lockMap.computeIfAbsent(lock, (k) -> {
            return new Object();
        });
        // 先使用本地锁
        synchronized (o) {
//            本地锁再次尝试获取结果，有就立马返回
            result = cache.get(key);
            if (result != null) {
                return (T) result.get();
            }
            return (T) invocation.proceed();
        }


    }
}
