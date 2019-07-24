package com.daiyanping.cms.Thread;

/**
 * @ClassName JoinTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class JoinTest {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1开始执行");
            Thread thread2 = new Thread(() -> {
                System.out.println("thread2执行");
            });
            thread2.start();
            try {
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("thread1结束执行");

        });



        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("主线程必须等到thread1执行完才执行");

    }
}
