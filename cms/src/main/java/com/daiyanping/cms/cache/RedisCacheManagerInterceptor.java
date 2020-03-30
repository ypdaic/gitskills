package com.daiyanping.cms.cache;

import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import sungo.cms.common.config.ApplicationContextProvider;
import sungo.util.enums.RedisKeyEnum;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RedisCacheManager 代理类
 * @author daiyanping
 */
@Data
@Service
public class RedisCacheManagerInterceptor extends AbstractCacheInterceptor {

    private RedisCacheManager redisCacheManager;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(4);

    @Override
    protected boolean matchMethod(Method method) {
        return method.getName().equals("getCache");
    }

    @Override
    protected Object invokeMethod(MethodInvocation invocation) throws Throwable {
        Object[] objects = invocation.getArguments();
        String key = objects[0].toString();
        Cache cache = this.cacheMap.get(key);
        if (cache != null) {
            return cache;
        }
        else {
            synchronized (this.cacheMap) {
//                    双重检查防止缓存重复创建
                cache = this.cacheMap.get(key);
                if (cache == null) {

                    Cache redisCache = (Cache) invocation.proceed();

                    ProxyFactory factory = new ProxyFactory();
                    factory.setExposeProxy(true);
                    RedisLockCacheInterceptor redisLockCacheInterceptor = ApplicationContextProvider.getBean(RedisLockCacheInterceptor.class);
                    LocalLockCacheInterceptor localLockCacheInterceptor = ApplicationContextProvider.getBean(LocalLockCacheInterceptor.class);
                    ArrayList<String> cacheNames = new ArrayList<>(1);
                    cacheNames.add(RedisKeyEnum.APP_CACHE.getPrefix());
                    RedisCacheTTLInterceptor redisCacheTTLInterceptor = ApplicationContextProvider.getBean(RedisCacheTTLInterceptor.class);
                    redisCacheTTLInterceptor.setCacheNames(cacheNames);
                    factory.addAdvisor(new DefaultPointcutAdvisor(localLockCacheInterceptor));
                    factory.addAdvisor(new DefaultPointcutAdvisor(redisLockCacheInterceptor));
                    factory.addAdvisor(new DefaultPointcutAdvisor(redisCacheTTLInterceptor));
                    factory.setTarget(redisCache);
                    cache = (Cache) factory.getProxy(Cache.class.getClassLoader());
                    cacheMap.put(key, cache);
                }
                return cache;
            }

        }
    }
}
