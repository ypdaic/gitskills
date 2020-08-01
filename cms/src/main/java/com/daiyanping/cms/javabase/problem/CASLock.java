package com.daiyanping.cms.javabase.problem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁实现
 */
public class CASLock {

    private volatile Thread thread = null;

    private static int value = 0;

    private AtomicReference<Thread> atomicReference = new AtomicReference();

    public void lock() {
        if (thread != null) {
            while (!atomicReference.compareAndSet( null, Thread.currentThread()));
        }
        thread = Thread.currentThread();
    }

    public void unlock() {
        atomicReference.compareAndSet( Thread.currentThread(), null);
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 1000; i++) {
            CASLock casLock = new CASLock();
            new Thread(() -> {
                casLock.lock();
                value++;
                casLock.unlock();
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();

        System.out.println(value);
    }


}
