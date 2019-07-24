package com.daiyanping.cms.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ReentrantLockPool
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class WaitAndNotifyPool {

    private List<MyConnection> pool = new ArrayList<MyConnection>();

    private int maxPoolSize;

    private AtomicInteger activeSize = new AtomicInteger();

    private Object o = new Object();

    public WaitAndNotifyPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public MyConnection getConnection() {
        MyConnection myConnection = null;
        synchronized (o) {
            if (pool.size() == 0 && activeSize.get() < maxPoolSize) {
                System.out.println("新增连接");
                myConnection = new MyConnection();
            } else if(activeSize.get() == maxPoolSize) {
                // 激活数等于最大连接数需要等待连接释放
                // 如果等待状态释放后，如果线程池为空则继续等待
                while (pool.isEmpty()) {
                    try {
                        o.wait();
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
        return myConnection;
    }

    public MyConnection getConnectionTimeOut(long time) {
        MyConnection myConnection = null;
        synchronized (o) {
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
//                        System.out.println(time);
                        o.wait(time);
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
        return myConnection;
    }

    public void releaseConnection(MyConnection myConnection) {
        synchronized (o) {
            pool.add(myConnection);
            o.notifyAll();
            System.out.println("唤醒等待的线程");
            activeSize.decrementAndGet();
        }
    }

    public static void main(String[] agrs) {
        AtomicInteger get = new AtomicInteger();
        AtomicInteger noGet = new AtomicInteger();
        WaitAndNotifyPool waitAndNotifyPool = new WaitAndNotifyPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(50);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(51);
        for (int i = 0; i < 50; i++) {
//            new Thread(new Work(countDownLatch, i, waitAndNotifyPool)).start();
            new Thread(new Work2(countDownLatch, i, waitAndNotifyPool, get, noGet, cyclicBarrier)).start();
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

        private WaitAndNotifyPool waitAndNotifyPool;

        public Work(CountDownLatch countDownLatch, int name, WaitAndNotifyPool waitAndNotifyPool) {
            this.countDownLatch = countDownLatch;
            this.name = name;
            this.waitAndNotifyPool = waitAndNotifyPool;
        }

        @Override
        public void run() {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MyConnection connection = waitAndNotifyPool.getConnection();
            System.out.println("线程" + name + "获得连接");
            Random r = new Random();
            int i = r.nextInt(1000);
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitAndNotifyPool.releaseConnection(connection);
            System.out.println("线程" + name + "释放连接");
        }
    }

    private static class Work2 implements Runnable {

        private CountDownLatch countDownLatch;

        private int name;

        private WaitAndNotifyPool waitAndNotifyPool;

        private AtomicInteger get;

        private AtomicInteger noGet;

        private CyclicBarrier cyclicBarrier;

        public Work2(CountDownLatch countDownLatch, int name, WaitAndNotifyPool waitAndNotifyPool, AtomicInteger get, AtomicInteger noGet, CyclicBarrier cyclicBarrier) {
            this.countDownLatch = countDownLatch;
            this.name = name;
            this.waitAndNotifyPool = waitAndNotifyPool;
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
            MyConnection connection = waitAndNotifyPool.getConnectionTimeOut(i);
            if (connection != null) {

                System.out.println("线程" + name + "获得连接");

                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitAndNotifyPool.releaseConnection(connection);
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



