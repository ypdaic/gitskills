package com.daiyanping.cms.javabase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {

    public static void main(String[] args) throws InterruptedException {
        VolatileAtomicTest.MyData myData = new VolatileAtomicTest.MyData();
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myData.addToPlus();
                }
                countDownLatch.countDown();

            }).start();

        }

        countDownLatch.await();
        System.out.println("线程数" + Thread.activeCount());
        System.out.println("主线程获取结果" + myData.getNumber());
    }

    public static class MyData {

        // 原子操作
        private AtomicInteger number = new AtomicInteger(0);

        /**
         * 保证原子性
         */
        public void addToPlus() {
            number.getAndIncrement();
        }

        public int getNumber() {
            return this.number.get();
        }
    }
}
