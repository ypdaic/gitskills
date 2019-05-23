package com.daiyanping.cms;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * CompletableFuture 的then相关方法，为其添加一个获取结果的监听器，监听器的执行是同步执行还是异步执行，取决于
 * 我们使用哪个方法
 */
public class CompletableFutureTests {

    @Test
    public void test() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        // 为future添加一个监听器
        future.thenRun(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    Thread thread = Thread.currentThread();
                    System.out.println(thread.getId());
                    System.out.println("Got value: " + future.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        System.out.println("Current state: " + future.isDone());
        future.complete("my value");
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId());
        /**
         * 如果存在监听器，主线程必须等监听器执行完后，才能执行,说明了调用complete方法的线程和回调是同一个线程
         */
        System.out.println("Current state: " + future.isDone());
    }

    @Test
    public void test2() {
        CompletableFuture<String> future = new CompletableFuture<>();

        future.thenAccept(new Consumer<String>() {
            @Override
            public void accept(String value) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread thread = Thread.currentThread();
                System.out.println(thread.getId());
                System.out.println("Got value: " + value);
            }
        });

        System.out.println("Current state: " + future.isDone());
        future.complete("my value");
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId());
        /**
         * 如果存在监听器，主线程必须等监听器执行完后，才能执行，说明了调用complete方法的线程和回调是同一个线程
         */
        System.out.println("Current state: " + future.isDone());
    }

    /**
     * 主线程和监听器是异步执行的
     */
    @Test
    public void test3() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        // 为future添加一个监听器
        future.thenRunAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    Thread thread = Thread.currentThread();
                    System.out.println(thread.getId());
                    System.out.println("Got value: " + future.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        System.out.println("Current state: " + future.isDone());
        future.complete("my value");
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId());

        System.out.println("Current state: " + future.isDone());
    }
}
