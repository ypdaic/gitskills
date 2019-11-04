package com.daiyanping.cms.aop;

import com.daiyanping.cms.annotation.RetryCountCheck;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @ClassName RetryCountCheckAop
 * @Description TODO 接口重试检查aop
 * @Author daiyanping
 * @Date 2019-11-01
 * @Version 0.1
 */
@Order(-3)
@Aspect
@Component
@Slf4j
public class RetryCountCheckAop {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedisScript<Boolean> retryCountCheckScript;

    @Pointcut("@annotation(com.daiyanping.cms.annotation.RetryCountCheck)")
    public void retryCountCheckPointcut() {

    }

    /**
     * 重试次数检查
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "retryCountCheckPointcut()")
    public Object retryCountCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = getKey(joinPoint);
        long cacheTime = getCacheTime(joinPoint);
        int count = getCount(joinPoint);

        ArrayList arrayList = new ArrayList(1);
        arrayList.add(key);
        boolean checkResult = (boolean) redisTemplate.execute(retryCountCheckScript, arrayList, cacheTime, count);
        if (!checkResult) {
            return null;
        }

        return joinPoint.proceed();
    }

    /**
     * 获取缓存key
     * @param joinPoint
     * @return
     */
    private String getKey(ProceedingJoinPoint joinPoint){
        RetryCountCheck retryCountCheck = GetAnnotationUtil.getAnnotation(joinPoint, RetryCountCheck.class);
        String cacheName = retryCountCheck.cacheName();
        String key = retryCountCheck.key();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Object[] arguments = joinPoint.getArgs();

        key = SpelExpressionUtil.parse(key, targetMethod, arguments, String.class);
        return cacheName + key;
    }

    /**
     * 获取缓存时间
     * @param joinPoint
     * @return
     */
    private long getCacheTime(ProceedingJoinPoint joinPoint){
        RetryCountCheck retryCountCheck = GetAnnotationUtil.getAnnotation(joinPoint, RetryCountCheck.class);
        return retryCountCheck.cacheTime();

    }

    /**
     * 获取重试次数
     * @param joinPoint
     * @return
     */
    private int getCount(ProceedingJoinPoint joinPoint) {
        RetryCountCheck retryCountCheck = GetAnnotationUtil.getAnnotation(joinPoint, RetryCountCheck.class);
        return retryCountCheck.count();

    }
}
