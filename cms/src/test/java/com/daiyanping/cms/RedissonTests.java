package com.daiyanping.cms;

import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.redisson.RedissonConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
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
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class, SpringDataWebAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
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

    /**
     * RMap使用，没有元素淘汰，数据分片，本地缓存功能
     */
    @Test
    public void test5() {
        RMap<String, User> map = redissonClient.getMap("RMap");
        User user = new User();
        user.setPassword("124");
        user.setAge(29);
        User prevObject = map.put("123", user);
//        User currentObject = map.putIfAbsent("323", new User());
//        User obj = map.remove("123");
    }

    /**
     * RMapCache使用，没有数据分片，本地缓存功能，提供元素淘汰功能，但由于
     * redis本身对hash类型的属性没法设置过期时间
     * 因此所有过期元素都是通过org.redisson.EvictionScheduler实例来实现定期清理的。
     * 为了保证资源的有效利用，每次运行最多清理300个过期元素。任务的启动时间将根据上次实际清理数量自动调整，
     * 间隔时间趋于1秒到1小时之间。比如该次清理时删除了300条元素，那么下次执行清理的时间将在1秒以后（最小间隔时间）。
     * 一旦该次清理数量少于上次清理数量，时间间隔将增加1.5倍。
     */
    @Test
    public void test6() {
        RMapCache<String, User> map = redissonClient.getMapCache("RMapCache");
        User user = new User();
        user.setPassword("124");
        user.setAge(29);
        // 有效时间 ttl = 10分钟
        map.put("key1", user, 10, TimeUnit.MINUTES);
        // 有效时间 ttl = 10分钟, 最长闲置时间 maxIdleTime = 10秒钟
        map.put("key2", user, 10, TimeUnit.MINUTES, 10, TimeUnit.SECONDS);

         // 有效时间 = 20 秒钟
        map.putIfAbsent("key3", user, 20, TimeUnit.SECONDS);
        // 有效时间 ttl = 40秒钟, 最长闲置时间 maxIdleTime = 10秒钟
        map.putIfAbsent("key4", user, 40, TimeUnit.SECONDS, 10, TimeUnit.SECONDS);
    }

    /**
     * RLocalCachedMap 不提供元素淘汰功能，在Redis缓存数据外，而外提供本地缓存功能，相当于提供了一二级缓存能力
     * RLocalCachedMapCache 提供元素淘汰 功能仅限于Redisson PRO版本
     *
     *
     *
     */
    @Test
    public void test7() throws ExecutionException, InterruptedException {
        LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
                // 用于淘汰清除本地缓存内的元素
                // 共有以下几种选择:
                // LFU - 统计元素的使用频率，淘汰用得最少（最不常用）的。
                // LRU - 按元素使用时间排序比较，淘汰最早（最久远）的。
                // SOFT - 元素用Java的WeakReference来保存，缓存元素通过GC过程清除。
                // WEAK - 元素用Java的SoftReference来保存, 缓存元素通过GC过程清除。
                // NONE - 永不淘汰清除缓存元素。
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.NONE)
                // 如果缓存容量值为0表示不限制本地缓存容量大小
                .cacheSize(1000)
                // 以下选项适用于断线原因造成了未收到本地缓存更新消息的情况。
                // 断线重连的策略有以下几种：
                // CLEAR - 如果断线一段时间以后则在重新建立连接以后清空本地缓存
                // LOAD - 在服务端保存一份10分钟的作废日志
                //        如果10分钟内重新建立连接，则按照作废日志内的记录清空本地缓存的元素
                //        如果断线时间超过了这个时间，则将清空本地缓存中所有的内容
                // NONE - 默认值。断线重连时不做处理。
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE)
                // 以下选项适用于不同本地缓存之间相互保持同步的情况
                // 缓存同步策略有以下几种：
                // INVALIDATE - 默认值。当本地缓存映射的某条元素发生变动时，同时驱逐所有相同本地缓存映射内的该元素
                // UPDATE - 当本地缓存映射的某条元素发生变动时，同时更新所有相同本地缓存映射内的该元素
                // NONE - 不做任何同步处理
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
                // 每个Map本地缓存里元素的有效时间，默认毫秒为单位
                .timeToLive(10000)
                // 或者
                .timeToLive(10, TimeUnit.SECONDS)
                // 每个Map本地缓存里元素的最长闲置时间，默认毫秒为单位
                .maxIdle(10000)
                // 或者
                .maxIdle(10, TimeUnit.SECONDS);

        RLocalCachedMap<String, Integer> map = redissonClient.getLocalCachedMap("RLocalCachedMap", options);

        Integer prevObject = map.put("123", 1);
        System.out.println(prevObject);
        Integer currentObject = map.putIfAbsent("323", 2);
        System.out.println(currentObject);
        Integer obj = map.remove("123");


        // 在不需要旧值的情况下可以使用fast为前缀的类似方法
        map.fastPut("a", 1);
        System.out.println(map.get("a"));
        map.fastPutIfAbsent("d", 32);
        map.fastRemove("b");

        RFuture<Integer> putAsyncFuture = map.putAsync("321", 4);
        Integer integer = putAsyncFuture.get();
        System.out.println(integer);
        RFuture<Boolean> fastPutAsyncFuture = map.fastPutAsync("321", 5);

    }

    /**
     * MapOptions提供了而外的持久化能力，可以将数据持久化到外部，需要
     * 开发者自己实现，提供异步的持久化，同步的持久化
     *
     *
     */
    @Test
    public void test8() throws ExecutionException, InterruptedException {
        MapOptions<String, String> options = MapOptions.<String, String>defaults()
                .writer(new MyMapWriter())
                .loader(new MyMapLoader());

        RMap<String, String> map = redissonClient.getMap("MapOptions", options);

        map.put("test", "test2");
        map.put("test", "test2");

    }

    static class MyMapWriter implements MapWriter {

        @Override
        public void write(Map map) {
            map.forEach((key, value) -> {
                System.out.println(key);
                System.out.println(value);
            });

        }

        @Override
        public void delete(Collection keys) {

        }
    }

    static class MyMapLoader implements MapLoader {
        @Override
        public Object load(Object key) {
            return null;
        }

        @Override
        public Iterable loadAllKeys() {
            return null;
        }
    }






}
