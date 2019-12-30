package com.daiyanping.cms.AQS;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @ClassName StampedLockTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-26
 * @Version 0.1
 */
public class StampedLockTest {

    private static int x;

    private static int y;

    final static StampedLock sl = new StampedLock();

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) throws InterruptedException {
//        test1();
        test2();
    }

    /**
     * 乐观读锁，悲观读锁使用
     */
    public static void test() {

        // 乐观读
        long stamp = sl.tryOptimisticRead();

        // 读入局部变量
        // 读的过程数据可能被修改
        int curX = x, curY = y;

        // 判断执行读操作期间，是否存在写操作，如果存在，则sl.validate 返回false
        if (!sl.validate(stamp)) {
            // 升级为悲观读锁
            stamp = sl.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                // 释放悲观锁
                sl.unlockRead(stamp);
            }
            Math.sqrt(curX * curX + curY * curY);
        }

    }

    /**
     * 验证：如果线程阻塞在 StampedLock 的 readLock() 或者 writeLock() 上时，此时调用该阻塞线程的 interrupt() 方法，会导致 CPU 飙升
     *
     * 使用 StampedLock 一定不要调用中断操作，如果需要支持中断功能，一定使用可中断的 悲观读锁 readLockInterruptibly() 和写锁 writeLockInterruptibly()
     * @throws InterruptedException
     */
    public static void test1() throws InterruptedException {
        Thread thread = new Thread(() -> {
            // 获取写锁
            sl.writeLock();

            // 永远阻塞在这里，不释放写锁
            LockSupport.park();
        });

        thread.start();

        // 保证thread获取写锁
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread1 = new Thread(() -> {
            // 阻塞在悲观读锁
            sl.readLock();
        });

        thread1.start();
        // 保证thread1阻塞在读锁
        Thread.sleep(100);

        // 中断线程thread1
        // 会导致线程thread1所在CPU飙升
        thread1.interrupt();
        thread1.join();
    }

    /**
     * ReentrantReadWriteLock不会导致CPU飙升
     * @throws InterruptedException
     */
    public static void test2() throws InterruptedException {
        Thread thread = new Thread(() -> {
            // 获取写锁
            writeLock.lock();

            // 永远阻塞在这里，不释放写锁
            LockSupport.park();
        });

        thread.start();

        // 保证thread获取写锁
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread1 = new Thread(() -> {
            // 阻塞在读锁
            readLock.lock();
        });

        thread1.start();
        // 保证thread1阻塞在读锁
        Thread.sleep(1000 * 10);

        // 中断线程thread1
        System.out.println("开始中断");
        thread1.interrupt();
//        thread1.join();
    }
}
