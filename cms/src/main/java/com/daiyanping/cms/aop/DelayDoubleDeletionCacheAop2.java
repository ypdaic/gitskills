package com.daiyanping.cms.aop;

import com.daiyanping.cms.annotation.DelayDoubleDelete;
import com.daiyanping.cms.annotation.DelayDoubleDeletes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DelayDoubleDeletionCacheAop
 * @Description 缓存延时双删处理
 * @Author daiyanping
 * @Date 2019-09-23
 * @Version 0.1
 */
@Order(1)
@Aspect
@Component
@Slf4j
public class DelayDoubleDeletionCacheAop2 implements DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private volatile boolean running = true;

//    TODO 根据业务可能需要调整
    // 延时时间
    private static long DELETE_TIME = 5;

    @Autowired
    RedisTemplate redisTemplate;

    ApplicationContext applicationContext;

    @Autowired
    RedissonClient redissonClient;

    RBlockingQueue<String> blockingQueue;

    RDelayedQueue<String> delayedQueue;

    @Pointcut("@annotation(com.daiyanping.cms.annotation.DelayDoubleDelete)")
    public void deletePointcut() {

    }

    @Pointcut("@annotation(com.daiyanping.cms.annotation.DelayDoubleDeletes)")
    public void deletePointcuts() {

    }

    /**
     * 后置处理，用于业务方法之后执行
     * @param joinPoint
     */
    @After(value = "deletePointcut() || deletePointcuts()")
    public void deleteAfter(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        DelayDoubleDelete delayDoubleDelete = AnnotationUtils.findAnnotation(targetMethod, DelayDoubleDelete.class);
        if (delayDoubleDelete != null) {

            String redisKey = getRedisKey(joinPoint, delayDoubleDelete);
            supportTransaction(delayDoubleDelete.transactionAware(), delayDoubleDelete.cacheNames() + redisKey);

        }

        DelayDoubleDeletes delayDoubleDeletes = AnnotationUtils.findAnnotation(targetMethod, DelayDoubleDeletes.class);
        if (delayDoubleDeletes != null) {
            DelayDoubleDelete[] deletes = delayDoubleDeletes.delete();
            for (int i = 0; i < deletes.length; i++) {
                String redisKey = getRedisKey(joinPoint, deletes[i]);
                supportTransaction(delayDoubleDeletes.transactionAware(), delayDoubleDeletes.cacheNames() + redisKey);
            }
        }
    }

    /**
     * 前置处理，用于业务方法之前执行，删除以前的缓存
     * @param joinPoint
     */
    @Before(value = "deletePointcut() || deletePointcuts()")
    public void deleteBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        DelayDoubleDelete delayDoubleDelete = AnnotationUtils.findAnnotation(targetMethod, DelayDoubleDelete.class);
        if (delayDoubleDelete != null && delayDoubleDelete.beforeInvocation()) {

            String redisKey = getRedisKey(joinPoint, delayDoubleDelete);
            supportTransaction(delayDoubleDelete.transactionAware(), delayDoubleDelete.cacheNames() + redisKey);

        }

        DelayDoubleDeletes delayDoubleDeletes = AnnotationUtils.findAnnotation(targetMethod, DelayDoubleDeletes.class);
        if (delayDoubleDeletes != null) {
            if (delayDoubleDeletes.beforeInvocation()) {

                DelayDoubleDelete[] deletes = delayDoubleDeletes.delete();
                for (int i = 0; i < deletes.length; i++) {
                    String redisKey = getRedisKey(joinPoint, deletes[i]);
                    supportTransaction(delayDoubleDeletes.transactionAware(), delayDoubleDeletes.cacheNames() + redisKey);
                }
            } else {
                DelayDoubleDelete[] deletes = delayDoubleDeletes.delete();
                for (int i = 0; i < deletes.length; i++) {
                    if (deletes[i].beforeInvocation()) {

                        String redisKey = getRedisKey(joinPoint, deletes[i]);
                        supportTransaction(delayDoubleDeletes.transactionAware(), delayDoubleDeletes.cacheNames() + redisKey);
                    }
                }
            }
        }
    }

    /**
     * 事物支持
     * @param transactionAware
     * @param redisKey
     */
    private void supportTransaction(boolean transactionAware, String redisKey) {
        if (transactionAware && TransactionSynchronizationManager.isSynchronizationActive()) {

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    putQueue(redisKey);
                }
            });

        }
        else {
            putQueue(redisKey);
        }
    }

    private String getRedisKey(JoinPoint joinPoint, DelayDoubleDelete delayDoubleDelete){
        Signature signature = joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        String spel = delayDoubleDelete.key();
        String keyGenerator = delayDoubleDelete.keyGenerator();
        if (StringUtils.isNotEmpty(spel)) {

            return SpelExpressionUtil.parse(spel, targetMethod, arguments, String.class);
        } else if (StringUtils.isNotEmpty(keyGenerator)) {
            KeyGenerator bean = (KeyGenerator) applicationContext.getBean(keyGenerator);
            return bean.generate(target, targetMethod, arguments).toString();
        } else {
            return "";
        }
    }

    @Override
    public void destroy() throws Exception {
        running = false;
    }

    private void putQueue(String redisKey) {
        if (StringUtils.isNotEmpty(redisKey)) {
            delayedQueue.offer(redisKey, DELETE_TIME, TimeUnit.SECONDS);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue("delay_double_delete_queue");
        this.blockingQueue = blockingQueue;
        this.delayedQueue = redissonClient.getDelayedQueue(blockingQueue);

        new Thread(() -> {
            Thread.currentThread().setName("DELAY_DOUBLE_DELETE_TASK");
            while (running) {
                String deleteKeyInfo = null;
                try {
                    deleteKeyInfo = blockingQueue.take();
                    redisTemplate.delete(deleteKeyInfo);
                } catch (Exception e) {
                    log.error("删除缓存异常!", e);
                    putQueue(deleteKeyInfo);
                }
            }

            //销毁
            delayedQueue.destroy();
        }).start();
    }

}
