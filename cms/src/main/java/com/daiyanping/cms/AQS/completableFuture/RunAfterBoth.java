package com.daiyanping.cms.AQS.completableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * 类说明：运行后执行类
 */
public class RunAfterBoth {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            SleepTools.second(1);
            System.out.println("ssss");
            return "s1";
        }).runAfterBothAsync(CompletableFuture.supplyAsync(() -> {
            SleepTools.second(2);
            System.out.println("ssss ");
            return "s2";
        }), () -> System.out.println("hello world"));
        SleepTools.second(3);
    }
}
