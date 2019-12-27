package com.daiyanping.cms.AQS;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @ClassName AtomicIntegerFieldUpdaterTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-29
 * @Version 0.1
 */
public class AtomicIntegerFieldUpdaterTest {

    private volatile int id;

    // AtomicIntegerFieldUpdater更新的属性不受private控制
    private final static AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterTest> atom = AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterTest.class, "id");

    private AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        AtomicIntegerFieldUpdaterTest atomicIntegerFieldUpdaterTest = new AtomicIntegerFieldUpdaterTest();
        CountDownLatch countDownLatch = new CountDownLatch(50);
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                    AtomicIntegerFieldUpdaterTest.atom.incrementAndGet(atomicIntegerFieldUpdaterTest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(AtomicIntegerFieldUpdaterTest.atom.incrementAndGet(atomicIntegerFieldUpdaterTest));
//                System.out.println(atomicIntegerFieldUpdaterTest.count.incrementAndGet());

            }).start();

        }
    }
}
