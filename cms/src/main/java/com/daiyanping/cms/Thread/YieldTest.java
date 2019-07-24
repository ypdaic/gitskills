package com.daiyanping.cms.Thread;

/**
 * @ClassName YieldTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class YieldTest {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            // yield方法使当前线程让出cpu资源
            Thread.yield();
            System.out.println("thread1 多半后执行");

        });

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 多半先执行");
        });
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
