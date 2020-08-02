package com.daiyanping.cms.jvm;

import lombok.AllArgsConstructor;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 测试软引用
 */
public class WeakReferenceTest {


    public static void main(String[] args) {
//        test1();
        test2();
    }

    /**
     *  弱引用只要gc就会被回收
     */
    public static void test1() {
        User daiyanping = new User("daiyanping", "123456");
        WeakReference<User> weakReference = new WeakReference<User>(daiyanping);
        // 将强引用清除
        daiyanping = null;
        System.gc();
        System.out.println("gc后查看是否存在" + weakReference.get());

    }

    /**
     *  弱引用只要gc就会被回收
     */
    public static void test2() {
        User daiyanping = new User("daiyanping", "123456");

        // 被回收前将被放到引用队列
        ReferenceQueue<User> userReferenceQueue = new ReferenceQueue<>();
        WeakReference<User> weakReference = new WeakReference<User>(daiyanping, userReferenceQueue);

        // gc 前可以获取
        System.out.println(daiyanping);
        // gc 前可以获取
        System.out.println(weakReference.get());
        // gc 前不可获取
        System.out.println(userReferenceQueue.poll());
        // 将强引用清除
        daiyanping = null;
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("gc后查看是否存在" + weakReference.get());
        System.out.println("gc后查看是否存在" + userReferenceQueue.poll());

    }


    @AllArgsConstructor
    static class User {
        private String name;

        private String password;
    }
}
