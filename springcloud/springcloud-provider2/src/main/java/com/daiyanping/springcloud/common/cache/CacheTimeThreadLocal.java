package com.daiyanping.springcloud.common.cache;

/**
 * 一级缓存和二级缓存，缓存时间同步
 */
public class CacheTimeThreadLocal {

    private final static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static long getCacheTime() {
        return threadLocal.get();
    }

    public static void setCacheTime(Long cacheTime) {
        threadLocal.set(cacheTime);
    }

    public static void remvoe() {
        threadLocal.remove();
    }
}
