package com.daiyanping.cms.jvm;

import lombok.AllArgsConstructor;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 测试软引用
 */
public class WeakReferenceTest {


    public static void main(String[] args) {
        test1();
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


    @AllArgsConstructor
    static class User {
        private String name;

        private String password;
    }
}
