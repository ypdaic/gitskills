package com.daiyanping.cms.AQS;

import java.util.concurrent.CountDownLatch;

public class CountDownLauchTest {

    /**
     * await 方法判断信号量是否不为0，不是的话就进行阻塞，countDown方法先将信号量减一，直到等于0后，然后释放第一个Node,第一个Node被释放后
     * 会进行下个Node的释放，最前面的线程也会协助释放，直到head节点不在变化，否则跳出for循环，执行业务方法
     * @param args
     */
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);

            Thread thread1 = new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    System.out.println("线程1开始释放资源");
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                    System.out.println("线程1被释放");
                } catch (InterruptedException e) {

                }
            });

            Thread thread2 = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2开始释放资源");
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                    System.out.println("线程2被释放");
                } catch (InterruptedException e) {

                }
            });

            Thread thread3 = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程3开始释放资源");
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                    System.out.println("线程3被释放");
                } catch (InterruptedException e) {

                }
            });

            thread1.start();

            thread2.start();

            thread3.start();

    }
}
