package com.daiyanping.cms.javabase;

import java.util.concurrent.Semaphore;

/**
 * @ClassName SemaphoreDemo
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-12
 * @Version 0.1
 */
public class SemaphoreDemo {

    private static Semaphore semaphore1 = new Semaphore(0);
    private static Semaphore semaphore2 = new Semaphore(0);
    private static Semaphore semaphore3 = new Semaphore(0);
    public static void main(String[] args) {
        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore3.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore3.release();
                System.out.println("产品经理规划新需求");
                semaphore1.release();
            }
        });

        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore1.acquire();
                    System.out.println("开发人员开发新需求功能");
                    semaphore2.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore2.acquire();
                    semaphore2.release();
                    System.out.println("测试人员测试新功能");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("早上：");
        System.out.println("测试人员来上班了...");
        System.out.println("产品经理来上班了...");
        System.out.println("开发人员来上班了...");

        thread1.start();
        thread2.start();
        thread3.start();
        semaphore3.release();
    }
}
