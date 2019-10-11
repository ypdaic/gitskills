package com.daiyanping.cms.aop;

import com.daiyanping.cms.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName LockAop
 * @Description 锁支持
 * @Author daiyanping
 * @Date 2019-09-27
 * @Version 0.1
 */
@Order(2)
@Aspect
@Component
@Slf4j
public class LockAop {

    private final Map<String, Object> lockMap = new ConcurrentHashMap();

    @Autowired
    RedissonClient redissonClient;

    @Pointcut("@annotation(com.daiyanping.cms.annotation.Lock)")
    public void lockPointcut() {

    }

    @Around(value = "lockPointcut()")
    public Object addOperationLog(ProceedingJoinPoint joinPoint) {
        String lockKey = getLockKey(joinPoint);
        Object newLock = new Object();
        Object oldLock = lockMap.putIfAbsent(lockKey, newLock);
        if (oldLock != null) {
            newLock = oldLock;
        }
        Object result = null;
        // 先使用本地锁
        synchronized (newLock) {
//            然后使用分布式锁
            RLock fairLock = redissonClient.getFairLock(lockKey);
            fairLock.lock();
            try {

                try {
                    result = joinPoint.proceed();
                } catch (Throwable throwable) {
                    log.error("业务操作异常!", throwable);
                    throw new RuntimeException("业务操作异常!", throwable);
                }
            } catch (Throwable throwable) {
                throw new RuntimeException("业务操作异常!", throwable);
            } finally {
                if (fairLock.isHeldByCurrentThread()) {
                    fairLock.unlock();
                }
            }
        }
        return result;
    }

    private String getLockKey(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        Lock lock = AnnotationUtils.findAnnotation(targetMethod, Lock.class);
        String spel = lock.key();
        return SpelExpressionUtil.parse(spel, targetMethod, arguments, String.class);
    }

    /**
     * 清除本地锁,防止潜在内存泄漏风险
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearLockMap() {
        lockMap.clear();
    }
}
