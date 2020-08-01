package com.daiyanping.cms.javabase;

import java.util.concurrent.CountDownLatch;

/**
 * 原子性验证
 */
public class VolatileAtomicTest {

    /**
     * 验证volatile的原子性保证
     * 结果是保证不了
     *
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        MyData myData = new MyData();
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

        private volatile int number = 0;

        public void addTo60() {
            number = 60;
        }

        /**
         * volatile 不保证原子性
         * 可以通过synchronized解决
         * 可以用Ao
         */
        public synchronized void addToPlus() {
            number++;
        }

        public int getNumber() {
            return this.number;
        }
    }
}
