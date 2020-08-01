package com.daiyanping.cms.javabase;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SynchronousQueueTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-04
 * @Version 0.1
 */
public class SynchronousQueue2Test {

    /**
     * 源码https://www.jianshu.com/p/d5e2e3513ba3
     * @param args
     */
    public static void main(String[] args) {

        SynchronousQueue<String> strings = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "put 1");
                // put 操作必须等待一个take操作，否则阻塞
                strings.put("1");

                System.out.println(Thread.currentThread().getName() + "put 2");
                strings.put("2");

                System.out.println(Thread.currentThread().getName() + "put 3");
                strings.put("3");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "AAA").start();

        new Thread(() -> {
            try {
                Thread.currentThread().sleep(500);
                // take操作必须等待一个put操作，否则阻塞
                System.out.println(Thread.currentThread().getName() + strings.take());

                Thread.currentThread().sleep(500);
                System.out.println(Thread.currentThread().getName() + strings.take());

                Thread.currentThread().sleep(500);
                System.out.println(Thread.currentThread().getName() + strings.take());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "BBB").start();



    }


}
