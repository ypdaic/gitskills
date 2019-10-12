package com.daiyanping.cms.cache;

import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * @ClassName MyRedisCacheWriter
 * @Description TODO 没有什么作用，只是为了创建代理类
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
public class MyRedisCacheWriter implements RedisCacheWriter {
    @Override
    public void put(String name, byte[] key, byte[] value, Duration ttl) {

    }

    @Override
    public byte[] get(String name, byte[] key) {
        return new byte[0];
    }

    @Override
    public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
        return new byte[0];
    }

    @Override
    public void remove(String name, byte[] key) {

    }

    @Override
    public void clean(String name, byte[] pattern) {

    }
}
