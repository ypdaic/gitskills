package com.daiyanping.cms.Thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName WaitAndNotify
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class WaitAndNotify {

    private static Object o = new Object();

    private static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {

            test1();

        });

        thread1.start();

        Thread thread2 = new Thread(() -> {


            test2();
        });

        thread2.start();

    }

    private static void test1() {
        while (true) {
            synchronized (o) {
                if (count.get() > 20) {
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    count.incrementAndGet();
                    System.out.println("正在生产");
                }
            }
        }


    }

    private static void test2() {
        while (true) {
            synchronized (o) {
                if (count.get() > 0) {
                    count.decrementAndGet();
                    System.out.println("正在消费");
                } else {
                    o.notifyAll();
                }
            }
        }
    }
}
