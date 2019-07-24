package com.daiyanping.cms.Thread;

/**
 * @ClassName InterruptedTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class InterruptedTest {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {

//            test1();
            test2();
        });



        thread1.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.interrupt();


    }

    private static void test1() {
        // Thread.currentThread().isInterrupted() 判断当前线程是否被中段过，但不清空中断标志位
        while (!Thread.currentThread().isInterrupted()) {

            System.out.println("thread1执行中");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // sleep方法感知到当前线程被中断后，会抛出中断异常，并且清空中断标志，如果需要继续让线程中断的话就需要再次调用interrupt()方法
                System.out.println("当前线程是否中断过：" + Thread.currentThread().isInterrupted());
                // 再次触发中断
                Thread.currentThread().interrupt();
            }
        }
        // Thread.currentThread().isInterrupted() 方法并不会清空中断标志位，如果需要清除需要调用Thread.interrupted() 方法
        System.out.println("调用Thread.currentThread().isInterrupted()后，中断标志是否被清空：" + Thread.currentThread().isInterrupted());
    }

    private static void test2() {
        // Thread.interrupted() 判断当前线程是否被中段过，但会清空中断标志位
        while (!Thread.interrupted()) {

            System.out.println("thread1执行中");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // sleep方法感知到当前线程被中断后，会抛出中断异常，并且清空中断标志，如果需要继续让线程中断的话就需要再次调用interrupt()方法
                System.out.println("当前线程是否中断过：" + Thread.currentThread().isInterrupted());
                // 再次触发中断
                Thread.currentThread().interrupt();
            }
        }
        // Thread.interrupted() 方法并会清空中断标志位
        System.out.println("调用Thread.interrupted()后，中断标志是否被清空：" + Thread.interrupted());
    }

}
