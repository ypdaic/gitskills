package com.daiyanping.cms.AQS.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明：取最快转换类
 * applyToEither,applyToEitherAsync用于取两者执行最快的结果并使用该结果继续执行，有返回值
 *
 * 两个任务是并行，并且都会执行完
 *
 * 只支持supplyAsync
 */
public class ApplyToEither {
    public static void main(String[] args) {
        test2();
    }

    /**
     * 结果：
     * 线程id：10 s1
     * 线程id：10 结果s1
     * s1
     * 线程id：11 hello world
     */
    public static void test1() {
        String result = CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " s1");
            return "s1";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " hello world");
            return "hello world";
        }), s -> {
            System.out.println("线程id：" + Thread.currentThread().getId() + " 结果" + s);
            return s;
        }).join();
        System.out.println(result);

        SleepTools.second(10);
    }

    /**
     * 结果：
     * 线程id：10 s1
     * 线程id：13 结果s1
     * s1
     * 线程id：11 hello world
     */
    public static void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        String result = CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " s1");
            return "s1";
        }).applyToEitherAsync(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("线程id：" + Thread.currentThread().getId() + " hello world");
            return "hello world";
        }), s -> {
            System.out.println("线程id：" + Thread.currentThread().getId() + " 结果" + s);
            return s;
        }, executorService).join();
        System.out.println(result);

        SleepTools.second(10);
    }

}
