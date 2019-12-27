package com.daiyanping.cms.AQS.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明：取最快消费类
 */
public class AcceptEither {
    /**
     * acceptEither,acceptEitherAsync用于取两者执行最快的结果并使用该结果继续执行，没有返回值
     *
     * 两个任务是并行，并且都会执行完
     *
     * supplyAsync,runAsync都支持
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        test2();
//        test2();
    }

    /**
     * 结果：
     * 主线程
     * 线程id：10 s1
     * 线程id：10 结果s1
     * 线程id：11 hello world
     *
     * 执行aciton的线程和执行s1的线程是同一个
     */
    public static void test2() {
        CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " s1");
            return "s1";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " hello world");
            return "hello world";
        }), (s)-> {

            System.out.println("线程id：" + Thread.currentThread().getId() + " 结果" + s);
        });

        System.out.println("主线程");

        SleepTools.second(10);
    }

    /**
     * 结果：
     * 主线程
     * 线程id：10 s1
     * 线程id：13 结果s1
     * 线程id：11 hello world
     *
     * acceptEitherAsync和acceptEither的区别就是action可以异步执行
     */
    public static void test1() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " s1");
            return "s1";
        }).acceptEitherAsync(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " hello world");
            return "hello world";
        }), (s)-> {
            System.out.println("线程id：" + Thread.currentThread().getId() + " 结果" + s);
        }, executorService);

        System.out.println("主线程");

        SleepTools.second(10);

    }

    /**
     * 结果：
     * 主线程
     * 线程id：10 s1
     * 线程id：10 结果null
     * 线程id：11 hello world
     * 执行aciton的线程和执行s1的线程是同一个
     */
    public static void test3() {
        CompletableFuture.runAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " s1");

        }).acceptEither(CompletableFuture.runAsync(() -> {
            SleepTools.second(2);
            System.out.println("线程id：" + Thread.currentThread().getId() + " hello world");

        }), (s)-> {

            System.out.println("线程id：" + Thread.currentThread().getId() + " 结果" + s);
        });

        System.out.println("主线程");

        SleepTools.second(10);
    }
}
