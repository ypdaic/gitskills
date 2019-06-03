package com.daiyanping.cms;

import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.redisson.RedissonConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedissonTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-30
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(classes = {RedisConfig.class, MybatisMapperScanTest.class})
@ContextConfiguration(classes = {RedissonConfig.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class RedissonTests {

    Logger logger = LoggerFactory.getLogger(RedissonTests.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("testdddd", "hhhhhh");
    }

    @Test
    public void test2() {
        RLock lock = redissonClient.getLock("ddddddd");
        lock.lock(10, TimeUnit.MINUTES);
    }

    /**
     * redssion 限流
     */
    @Test
    public void test3() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("myRateLimiter");
        // 初始化
        // 最大流速 = 每1秒钟产生10个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);

        CountDownLatch latch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            Thread t = new Thread(() -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean success = rateLimiter.tryAcquire(1);
                if (success) {
                    logger.info("获取了令牌的线程：" + Thread.currentThread().getId());
                } else {
                    logger.info("没有获取了令牌的线程：" + Thread.currentThread().getId());
                }
            });
            t.start();
        }

        try {
            Thread.sleep(1000 * 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rateLimiter.delete();
    }

    @Test
    public void test4() {
        ExecutorOptions options = ExecutorOptions.defaults();

        // 指定重新尝试执行任务的时间间隔。
        // ExecutorService的工作节点将等待10分钟后重新尝试执行任务
        //
        // 设定为0则不进行重试
        //
        // 默认值为5分钟
        options.taskRetryInterval(10, TimeUnit.MINUTES);
        RExecutorService executorService = redissonClient.getExecutorService("myExecutor", options);
        MyThread myThread = new MyThread();
        executorService.submit(myThread);

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

     static class MyThread implements Runnable {

        @Override
        public void run() {
            System.out.println("sdfsfss");
        }
    }



}
