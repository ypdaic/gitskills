package com.daiyanping.cms.javabase.classload;

public class DeadThreadTest {

    public static void main(String[] args) {
        Runnable runable = () -> {
            System.out.println(Thread.currentThread().getName() + "开始");
            DeadThread deadThread = new DeadThread();
            System.out.println(Thread.currentThread().getName() + "结束");
        };

        Thread t1 = new Thread(runable, "线程1");
        Thread t2 = new Thread(runable, "线程2");
        t1.start();
        t2.start();



    }

    public static class DeadThread {

        /**
         * 只会有一个线程执行
         */
        static {
            if (true) {
                System.out.println(Thread.currentThread().getName() + "初始化当前类");
                while (true) {

                }
            }
        }

    }
}
