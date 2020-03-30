package com.daiyanping.cms.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.TimeUnit;

public class CaffeineTest {

    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
        test3();
    }

    /**
     * 手动方式
     * @throws Exception
     */
    public static void test1() throws Exception {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(3)
                .build();

        String key = "hello";
        String key2 = "func";
        cache.put(key, "world");
        Thread.sleep(1000 * 9);
        Object o = cache.getIfPresent(key);
        System.out.println(o);
    }

    /**
     * 同步方式
     * @throws Exception
     */
    public static void test2() throws Exception {
        LoadingCache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(3)
                .build(key -> createExpensiveGraph(key));

        String key = "name";
        // 采用同步方式去获取一个缓存和上面的手动方式是一个原理
        // 在build Cache的时候回提供一个createExpensiveGraph函数
        // 查询并在缺失的情况下使用同步的方式来构建一个缓存
        Object o = cache.get(key);
        System.out.println(o);
        Object o2 = cache.get(key);
        System.out.println(o2);
    }

    // get方法获取不到，就会执行该方法写入缓存
    public static Object createExpensiveGraph(String key) {
        return "test";

    }

    /**
     * 同步方式
     * @throws Exception
     */
    public static void test3() throws Exception {
        LoadingCache<String, Object> cache = Caffeine.newBuilder()
                 // 自定义过期策略
                .expireAfter(new Expiry<Object, Object>() {
                    @Override
                    public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
                        return currentTime + 1000 * 60  * 10;
                    }

                    @Override
                    public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    // 直接返回currentDuration表示不启用读过期设置
                    @Override
                    public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                })
                .maximumSize(3)
                .build(key -> createExpensiveGraph(key));

        String key = "name";
        // 采用同步方式去获取一个缓存和上面的手动方式是一个原理
        // 在build Cache的时候回提供一个createExpensiveGraph函数
        // 查询并在缺失的情况下使用同步的方式来构建一个缓存
        Object o = cache.get(key);
        System.out.println(o);
        Object o2 = cache.get(key);
        System.out.println(o2);
        Object o3 = cache.get(key);
        System.out.println(o3);
    }
}
