package com.daiyanping.cms.AQS;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName MyFutureTaskTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class MyFutureTaskTest {

    public static void main(String[] args) {
        test2();
//        test1();

    }

    public static void test1() {
        MyFutureTask myFutureTaskTest = new MyFutureTask<Object>(() ->{
            Thread.sleep(1000);
            return "这是我的futureTask";
        });
        new Thread(myFutureTaskTest).start();
        long l = System.currentTimeMillis();
        System.out.println("开始获取任务" + l);
        try {
            Object o = myFutureTaskTest.get();
            System.out.println(o);
            System.out.println("结束获取任务,等待时间" + (System.currentTimeMillis() - l));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        MyFutureTask myFutureTaskTest = new MyFutureTask<Object>(() ->{
            Thread.sleep(5000);
            return "这是我的futureTask";
        });
        new Thread(myFutureTaskTest).start();
        long l = System.currentTimeMillis();
        System.out.println("开始获取任务" + l);
        try {
            Object o = myFutureTaskTest.get(6000, TimeUnit.MILLISECONDS);
            System.out.println(o);
            System.out.println("结束获取任务" + (System.currentTimeMillis() - l));
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
