package com.daiyanping.cms.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName RedisCacheProxy
 * @Description TODO RedisCache代理类,实现锁的细粒度划分,支持分布式锁
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@Data
@Slf4j
public class RedisCacheProxy implements MethodInterceptor {

    private final Map<String, Object> lockMap = new ConcurrentHashMap();

    private Cache redisCache;

    private RedissonClient redissonClient;

    private String name;

    // 定时清理本地锁,防止内存溢出
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private static String lockSuffix = "~lock";

    public RedisCacheProxy(Cache redisCache, RedissonClient redissonClient, String name, ThreadPoolTaskScheduler threadPoolTaskScheduler){
        this.redisCache = redisCache;
        this.name = name;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        threadPoolTaskScheduler.schedule(() -> {
            lockMap.clear();
        }, new CronTrigger("0 0 0 * * ?"));
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().equals("get") && method.getParameterCount() == 2) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes[0].isAssignableFrom(Object.class) && parameterTypes[0].isAssignableFrom(Callable.class)) {
                return get(objects[0], (Callable<T>)objects[1]);
            }
        }

        return method.invoke(redisCache, objects);

    }

    /**
     * 重新实现同步的get方法
     * @param key
     * @param valueLoader
     * @param <T>
     * @return
     */
    public <T> T get(Object key, Callable<T> valueLoader) {
        // 获取到结果立马返回
        Cache.ValueWrapper result = redisCache.get(key);
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
            result = redisCache.get(key);
            if (result != null) {
                return (T) result.get();
            }
//            然后使用分布式锁
            RLock fairLock = redissonClient.getFairLock(lock);
            fairLock.lock();
            try {
//              分布式锁再次尝试获取结果，有就立马返回
                result = redisCache.get(key);
                if (result != null) {
                    return (T) result.get();
                }
                T value = valueFromLoader(key, valueLoader);
                redisCache.put(key, value);
                return value;

            } catch (Exception e) {
                throw new RuntimeException("缓存获取失败!", e);
            } finally {
                if (fairLock.isHeldByCurrentThread()) {
                    fairLock.unlock();
                }
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
