package com.daiyanping.cms.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.Cache;
import sungo.util.exception.CommonException;

import java.util.concurrent.Callable;

/**
 * 分布式锁支持
 * @author daiyanping
 */
@Slf4j
@Data
public class RedisLockCacheInterceptor extends AbstractCacheLockInterceptor{

    private Cache cache;

    private RedissonClient redissonClient;

    private static String lockSuffix = "~lock";

    public RedisLockCacheInterceptor(Cache cache, RedissonClient redissonClient){
        this.cache = cache;
        this.redissonClient = redissonClient;
    }

    /**
     * 重新实现同步的get方法
     * @param key
     * @param valueLoader
     * @param <T>
     * @return
     */
    public <T> T get(Object key, Callable<T> valueLoader, MethodInvocation invocation) throws Throwable{
        String lock = key + lockSuffix;
//      使用分布式锁
        RLock fairLock = redissonClient.getFairLock(lock);
        fairLock.lock();
        Cache.ValueWrapper result = null;
        try {
//          分布式锁再次尝试获取结果，有就立马返回
            result = ((Cache) invocation.getThis()).get(key);
            if (result != null) {
                return (T) result.get();
            }
            T value = valueFromLoader(key, valueLoader);
            ((Cache) AopContext.currentProxy()).put(key, value);
            return value;

        } catch (Throwable e) {
            log.error("缓存获取失败", e);
            throw new CommonException("缓存获取失败!");
        } finally {
            if (fairLock.isHeldByCurrentThread()) {
                fairLock.unlock();
            }
        }
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {

        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new Cache.ValueRetrievalException(key, valueLoader, e);
        }
    }
}
