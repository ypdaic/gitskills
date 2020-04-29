package com.daiyanping.springcloud.common.cache;

import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

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
public class RedisCacheManagerInterceptor extends AbstractCacheInterceptor implements BeanFactoryAware {

    private RedisCacheManager redisCacheManager;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(4);

    private BeanFactory beanFactory;

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
                    String name = redisCache.getName();
                    ProxyFactory factory = new ProxyFactory();
                    factory.setExposeProxy(true);
                    RedisLockCacheInterceptor redisLockCacheInterceptor = beanFactory.getBean(RedisLockCacheInterceptor.class);
                    LocalLockCacheInterceptor localLockCacheInterceptor = beanFactory.getBean(LocalLockCacheInterceptor.class);
                    ArrayList<String> cacheNames = new ArrayList<>(1);
                    cacheNames.add("APP_CACHE");
                    RedisCacheTTLInterceptor redisCacheTTLInterceptor = beanFactory.getBean(RedisCacheTTLInterceptor.class);
                    redisCacheTTLInterceptor.setCacheNames(cacheNames);


                    String beanName = LocalFirstLevelCacheInterceptor.registerBeanDefinition(beanFactory, name);

                    LocalFirstLevelCacheInterceptor localFirstLevelCacheInterceptor = (LocalFirstLevelCacheInterceptor) beanFactory.getBean(beanName);

                    factory.addAdvisor(new DefaultPointcutAdvisor(localFirstLevelCacheInterceptor));
//                    factory.addAdvisor(new DefaultPointcutAdvisor(localLockCacheInterceptor));
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

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
