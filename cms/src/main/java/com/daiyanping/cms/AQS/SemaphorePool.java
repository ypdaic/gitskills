package com.daiyanping.cms.AQS;

import com.daiyanping.cms.Thread.MyConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName SemaphorePool
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-25
 * @Version 0.1
 */
public class SemaphorePool {

    private List<MyConnection> pool = new ArrayList<MyConnection>();

    private int maxPoolSize;

    private AtomicInteger activeSize = new AtomicInteger();

    private Semaphore semaphore;

    public SemaphorePool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        semaphore = new Semaphore(maxPoolSize);
    }

    public MyConnection getConnection() {
        MyConnection myConnection = null;
        try {
            semaphore.acquire();
            if (pool.size() == 0 && activeSize.get() < maxPoolSize) {
                System.out.println("新增连接");
                myConnection = new MyConnection();
            } else if(activeSize.get() == maxPoolSize) {
                // 激活数等于最大连接数需要等待连接释放
                // 如果等待状态释放后，如果线程池为空则继续等待
                while (pool.isEmpty()) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return pool.remove(0);
            } else {
                myConnection = pool.remove(0);
                System.out.println("直接获取连接");
            }
            activeSize.incrementAndGet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return myConnection;
    }

    public MyConnection getConnectionTimeOut(long time) {
        MyConnection myConnection = null;
        try {
            boolean b = semaphore.tryAcquire(time, TimeUnit.MILLISECONDS);
            if (b) {

                if (pool.size() == 0 && activeSize.get() < maxPoolSize) {
                    System.out.println("新增连接");
                    myConnection = new MyConnection();
                } else if(activeSize.get() == maxPoolSize) {
                    // 激活数等于最大连接数需要等待连接释放
                    // 如果等待状态释放后，如果线程池为空则继续等待
                    long waitTime = System.currentTimeMillis() + time;
                    while (pool.isEmpty()) {
                        time = waitTime - System.currentTimeMillis();
                        if (time <= 0) {
                            return null;
                        }
                        try {
                            semaphore.tryAcquire(time, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return pool.remove(0);

                } else {
                    myConnection = pool.remove(0);
                    System.out.println("直接获取连接");
                }
                activeSize.incrementAndGet();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return myConnection;
    }

    public void releaseConnection(MyConnection myConnection) {
        pool.add(myConnection);
        System.out.println("唤醒等待的线程");
        activeSize.decrementAndGet();
        semaphore.release();
    }

    public static void main(String[] agrs) {
        AtomicInteger get = new AtomicInteger();
        AtomicInteger noGet = new AtomicInteger();
        SemaphorePool semaphorePool = new SemaphorePool(5);
        CountDownLatch countDownLatch = new CountDownLatch(50);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(51);
        for (int i = 0; i < 50; i++) {
//            new Thread(new Work(countDownLatch, i, semaphorePool)).start();
            new Thread(new Work2(countDownLatch, i, semaphorePool, get, noGet, cyclicBarrier)).start();
        }

        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println(get.get() + "个线程成功获取连接");
        System.out.println(noGet.get() + "个线程失败获取连接");
        System.out.println("50个线程获取连接");
    }

    private static class Work implements Runnable {

        private CountDownLatch countDownLatch;

        private int name;

        private SemaphorePool semaphorePool;

        public Work(CountDownLatch countDownLatch, int name, SemaphorePool semaphorePool) {
            this.countDownLatch = countDownLatch;
            this.name = name;
            this.semaphorePool = semaphorePool;
        }

        @Override
        public void run() {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MyConnection connection = semaphorePool.getConnection();
            System.out.println("线程" + name + "获得连接");
            Random r = new Random();
            int i = r.nextInt(1000);
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphorePool.releaseConnection(connection);
            System.out.println("线程" + name + "释放连接");
        }
    }

    private static class Work2 implements Runnable {

        private CountDownLatch countDownLatch;

        private int name;

        private SemaphorePool semaphorePool;

        private AtomicInteger get;

        private AtomicInteger noGet;

        private CyclicBarrier cyclicBarrier;

        public Work2(CountDownLatch countDownLatch, int name, SemaphorePool semaphorePool, AtomicInteger get, AtomicInteger noGet, CyclicBarrier cyclicBarrier) {
            this.countDownLatch = countDownLatch;
            this.name = name;
            this.semaphorePool = semaphorePool;
            this.get = get;
            this.noGet = noGet;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Random r = new Random();
            int i = r.nextInt(1000);
            MyConnection connection = semaphorePool.getConnectionTimeOut(i);
            if (connection != null) {

                System.out.println("线程" + name + "获得连接");

                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphorePool.releaseConnection(connection);
                System.out.println("线程" + name + "释放连接");
                get.incrementAndGet();
            } else {
                System.out.println("线程" + name + "获得连接失败");
                noGet.incrementAndGet();
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
