package com.daiyanping.cms.AQS.completableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * 类说明：结合消费类
 */
public class ThenAcceptBoth {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("2");
            return "hello";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(2);
            System.out.println("1");
            return "world";
        }), (s1, s2) -> System.out.println(s1 + " " + s2));
        SleepTools.second(3);
    }
}
