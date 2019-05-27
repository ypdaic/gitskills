package com.daiyanping.cms;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.masterslave.StatefulRedisMasterSlaveConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName LettuceTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-23
 * @Version 0.1
 */
public class LettuceTests {

    // 1000个线程并发访问redis，共享一个redisClient
    private static final CountDownLatch countDownLatch = new CountDownLatch(1000);

    private static final RedisURI redisUrl = RedisURI.Builder.redis("127.0.0.1", 6379).build();

    private static final ClientResources clientResoure = DefaultClientResources.create();
    private static final RedisClient redisClient = RedisClient.create(clientResoure, redisUrl);


    /**
     * 没有并发问题，Lettuce是线程安全的
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        for(int i = 0; i < 1000; i++) {
            new Thread(new RedisSet()).start();
        }
        Thread.sleep(1000 * 10);
    }

    /**
     * 在主从模式下，仅使用主机
     * @throws InterruptedException
     */
    @Test
    public void test2() throws InterruptedException {
        RedisClient redisClient = RedisClient.create();

        StatefulRedisMasterSlaveConnection<String, String> connection = MasterSlave.connect(redisClient, new Utf8StringCodec(),
                redisUrl);
        connection.setReadFrom(ReadFrom.MASTER_PREFERRED);

        System.out.println("Connected to Redis");
        RedisAsyncCommands<String, String> async = connection.async();
        async.set("hello", "wordssss");
        connection.close();
        redisClient.shutdown();
    }

    /**
     * 哨兵模式
     * @throws InterruptedException
     */
    @Test
    public void test3() throws InterruptedException {
        RedisClient redisClient = RedisClient.create();

        RedisURI.Builder mymaster = RedisURI.Builder.sentinel("127.0.0.1", 26379, "mymaster");
        RedisURI build = mymaster.build();
        StatefulRedisMasterSlaveConnection<String, String> connection = MasterSlave.connect(redisClient, new Utf8StringCodec(),
                build);
        connection.setReadFrom(ReadFrom.SLAVE_PREFERRED);

        System.out.println("Connected to Redis");
        RedisAsyncCommands<String, String> async = connection.async();
        RedisFuture<String> hello = async.get("hello");
        hello.thenAcceptAsync(action -> {
            System.out.println(action);
        });

        connection.close();
        redisClient.shutdown();
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
            RedisAsyncCommands<String, String> async = redisClient.connect().async();
            async.set("hello", "word");


            RedisFuture<String> hello = async.get("hello");
            callback1(hello);
            callback2(hello);

        }

    }

    static void callback1(RedisFuture<String> hello) {
        try {
                /*
                    使用get方法并不能立马获取其结果，这里会阻塞，直到数据被塞到RedisFuture中
                    最好是给一个超时时间
                */
            String s = hello.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用监听器方式
     * @param hello
     */
    static void callback2(RedisFuture<String> hello) {
        hello.thenAccept(value -> {
            System.out.println(value);
        });
    }
}
