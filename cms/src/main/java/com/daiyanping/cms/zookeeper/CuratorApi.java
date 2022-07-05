package com.daiyanping.cms.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName CuratorApi
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-21
 * @Version 0.1
 */
public class CuratorApi {

    static CuratorFramework curatorFramework2;


    public static void main(String args[]) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1", 3000, 5000, retryPolicy);
        // 必须调用start
        curatorFramework.start();

        curatorFramework2 = curatorFramework;

        lock();
    }

    // 分布式锁使用
    public static void lock() throws Exception {

        InterProcessMutex lock = new InterProcessMutex(curatorFramework2, "/lock_test");
        if (lock.acquire(10, TimeUnit.SECONDS)) {
            try {
                // do some work
                Thread.sleep(1000);
                System.out.println("xxxxxxxxxxxx");
            } finally {
                lock.release();
            }
        }
    }
}
