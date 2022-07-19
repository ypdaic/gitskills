package com.daiyanping.cms.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * withProtection Curator 保护模式会在节点前面加一个guid，如果第一次创建节点后，节点数据没有返回成功，后续重试的时候
 * 会去搜索时候有包含该guid的节点，如果有就不创建了
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

//        lock();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            final int name = i;
            executorService.execute(() -> {
                leaderElection(curatorFramework, name);
            });
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
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

    // 分布式读锁使用
    public static void readLock() throws Exception {

        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework2, "/lock_test");
        InterProcessMutex interProcessMutex = interProcessReadWriteLock.readLock();
        if (interProcessMutex.acquire(10, TimeUnit.SECONDS)) {
            try {
                // do some work
                Thread.sleep(1000);
                System.out.println("xxxxxxxxxxxx");
            } finally {
                interProcessMutex.release();
            }
        }
    }

    // 分布式写锁使用
    public static void writeLock() throws Exception {

        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework2, "/lock_test");
        InterProcessMutex interProcessMutex = interProcessReadWriteLock.writeLock();
        if (interProcessMutex.acquire(10, TimeUnit.SECONDS)) {
            try {
                // do some work
                Thread.sleep(1000);
                System.out.println("xxxxxxxxxxxx");
            } finally {
                interProcessMutex.release();
            }
        }
    }

    // leader 选举 内部使分布式锁实现，谁抢到锁谁就是master，锁释放后，别人抢到别人就是master,takeLeadership方法不退出就会一直持有锁
    public static void leaderElection(CuratorFramework client, int i) {
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter()
        {
            // 该方法退出会放弃领导权限，触发重新选举
            public void takeLeadership(CuratorFramework client) throws Exception
            {
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership

                System.out.println("i'm leader now " + i);
                Thread.sleep(1000 * 10);
            }
        };

        LeaderSelector selector = new LeaderSelector(client, "/cachePreHeat_leader", listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
    }
}
