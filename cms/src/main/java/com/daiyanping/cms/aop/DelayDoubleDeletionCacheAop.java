package com.daiyanping.cms.aop;

import com.daiyanping.cms.annotation.DelayDoubleDelete;
import com.daiyanping.cms.annotation.DelayDoubleDeletes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

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
public class DelayDoubleDeletionCacheAop implements InitializingBean, DisposableBean, ApplicationContextAware {

    private  final AtomicLong sequencer = new AtomicLong();

    private  final BlockingQueue<DeleteKeyInfo> queue = new DelayQueue<>();

    private volatile boolean running = true;

//    TODO 根据业务可能需要调整
    // 延时时间
    private static long DELETE_TIME = 2 * 1000_000l;

    @Autowired
    RedisTemplate redisTemplate;

    ApplicationContext applicationContext;

    @Pointcut("@annotation(com.daiyanping.cms.annotation.DelayDoubleDelete)")
    public void deletePointcut() {

    }

    @Pointcut("@annotation(com.daiyanping.cms.annotation.DelayDoubleDeletes)")
    public void deletePointcuts() {

    }

    @After(value = "deletePointcut() || deletePointcuts()")
    public void delete(JoinPoint joinPoint) {
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
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            Thread.currentThread().setName("DELAY_DOUBLE_DELETE_TASK");
            while (running) {
                DeleteKeyInfo deleteKeyInfo = null;
                try {
                    deleteKeyInfo = queue.take();
                    redisTemplate.delete(deleteKeyInfo.key);
                } catch (InterruptedException e) {
                    log.error("获取队列数据发生中断!", e);
                } catch (Exception e) {
                    log.error("删除缓存异常!", e);
                    putQueue(deleteKeyInfo.key);
                }
            }
        }).start();

    }

    @Override
    public void destroy() throws Exception {
        running = false;
    }

    private void putQueue(String redisKey) {
        if (StringUtils.isNotEmpty(redisKey)) {

            DeleteKeyInfo deleteKeyInfo = new DeleteKeyInfo(redisKey, System.nanoTime() + DELETE_TIME, sequencer.getAndIncrement());
            queue.offer(deleteKeyInfo);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Data
    @AllArgsConstructor
    private static class DeleteKeyInfo implements Delayed {
        private String key;
        private long time;
        private long sequenceNumber;

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - now(), NANOSECONDS);
        }

        private long now() {
            return System.nanoTime();
        }

        @Override
        public int compareTo(Delayed other) {
            if (other == this)
                return 0;
            else {
                DeleteKeyInfo x = (DeleteKeyInfo)other;
                long diff = time - x.time;
                if (diff < 0)
                    return -1;
                else if (diff > 0)
                    return 1;
                else if (sequenceNumber < x.sequenceNumber)
                    return -1;
                else
                    return 1;
            }
        }
    }

}
