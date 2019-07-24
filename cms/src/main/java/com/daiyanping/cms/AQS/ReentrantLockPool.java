package com.daiyanping.cms.AQS;

import com.daiyanping.cms.Thread.MyConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ReentrantLockPool
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class ReentrantLockPool {

    private List<MyConnection> pool = new ArrayList<MyConnection>();

    private int maxPoolSize;

    private AtomicInteger activeSize = new AtomicInteger();

    private ReentrantLock lock = new ReentrantLock(true);

    private Condition wait = lock.newCondition();

    public ReentrantLockPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public MyConnection getConnection() {
        MyConnection myConnection = null;
        lock.lock();
        try {
            if (pool.size() == 0 && activeSize.get() < maxPoolSize) {
                System.out.println("新增连接");
                myConnection = new MyConnection();
            } else if(activeSize.get() == maxPoolSize) {
                // 激活数等于最大连接数需要等待连接释放
                // 如果等待状态释放后，如果线程池为空则继续等待
                while (pool.isEmpty()) {
                    try {
                        wait.await();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return myConnection;
    }

    public MyConnection getConnectionTimeOut(long time) {
        MyConnection myConnection = null;
        try {
            boolean b = lock.tryLock(time, TimeUnit.MILLISECONDS);
            if (b) {
                try {

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
                                wait.await(time, TimeUnit.MILLISECONDS);
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
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return myConnection;
    }

    public void releaseConnection(MyConnection myConnection) {
        lock.lock();
        try {
            pool.add(myConnection);
            // 每次只释放等待节点中第一个节点
            wait.signal();
            System.out.println("唤醒等待的线程");
            activeSize.decrementAndGet();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] agrs) {
        AtomicInteger get = new AtomicInteger();
        AtomicInteger noGet = new AtomicInteger();
        ReentrantLockPool reentrantLockPool = new ReentrantLockPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(50);
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(51);
        for (int i = 0; i < 50; i++) {
            new Thread(new Work(countDownLatch, i, reentrantLockPool)).start();
//            new Thread(new Work2(countDownLatch, i, reentrantLockPool, get, noGet, cyclicBarrier)).start();
        }

//        try {
//            cyclicBarrier.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (BrokenBarrierException e) {
//            e.printStackTrace();
//        }

        System.out.println(get.get() + "个线程成功获取连接");
        System.out.println(noGet.get() + "个线程失败获取连接");
        System.out.println("50个线程获取连接");
    }

    private static class Work implements Runnable {

        private CountDownLatch countDownLatch;

        private int name;

        private ReentrantLockPool reentrantLockPool;

        public Work(CountDownLatch countDownLatch, int name, ReentrantLockPool waitAndNotifyPool) {
            this.countDownLatch = countDownLatch;
            this.name = name;
            this.reentrantLockPool = waitAndNotifyPool;
        }

        @Override
        public void run() {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MyConnection connection = reentrantLockPool.getConnection();
            System.out.println("线程" + name + "获得连接");
            Random r = new Random();
            int i = r.nextInt(1000);
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reentrantLockPool.releaseConnection(connection);
            System.out.println("线程" + name + "释放连接");
        }
    }

    private static class Work2 implements Runnable {

        private CountDownLatch countDownLatch;

        private int name;

        private ReentrantLockPool reentrantLockPool;

        private AtomicInteger get;

        private AtomicInteger noGet;

        private CyclicBarrier cyclicBarrier;

        public Work2(CountDownLatch countDownLatch, int name, ReentrantLockPool reentrantLockPool, AtomicInteger get, AtomicInteger noGet, CyclicBarrier cyclicBarrier) {
            this.countDownLatch = countDownLatch;
            this.name = name;
            this.reentrantLockPool = reentrantLockPool;
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
            MyConnection connection = reentrantLockPool.getConnectionTimeOut(i);
            if (connection != null) {

                System.out.println("线程" + name + "获得连接");

                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reentrantLockPool.releaseConnection(connection);
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



