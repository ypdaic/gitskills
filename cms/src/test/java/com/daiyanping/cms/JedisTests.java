package com.daiyanping.cms;

import org.junit.Test;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 解决jedis多线程不安全问题，就需要使用连接池，每个操作独享一个jedis实例
 */
public class JedisTests {

    // 20个线程并发访问redis，共享一个jedis
    private static final CountDownLatch countDownLatch = new CountDownLatch(1000);

    private static final Jedis jedis = new Jedis("127.0.0.1", 6379);


    @Test
    public void test() {
        Client client = jedis.getClient();
        client.connect();
        for(int i = 0; i < 1000; i++) {
            new Thread(new RedisSet()).start();
        }
    }

    static class RedisSet implements Runnable{

        @Override
        public void run() {
            try {
                countDownLatch.countDown();
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jedis.set("hello", "world");

        }

    }
}
