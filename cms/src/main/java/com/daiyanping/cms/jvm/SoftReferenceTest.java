package com.daiyanping.cms.jvm;

import lombok.AllArgsConstructor;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * 测试软引用
 */
public class SoftReferenceTest {

    /**
     * -Xms10m -Xmx10m -XX:printGC
     * 软引用在内存不足时才会进行回收
     * @param args
     */
    public static void main(String[] args) {
        // 我们创建的User是放在堆中，OOM后就会被回收
//        test1();
        // 我们创建的String对象也是放在堆中，OOM后就会被回收
//        test2();

        // String sdfds = "sdfsf";这种形式是将字面量常量放在了字符串池中，GC不会被回收这块区域
//        所以出现的结果就是即使OOM了还是能够获取到
        test3();
    }

    public static void test1() {
        User daiyanping = new User("daiyanping", "123456");
        SoftReference<User> stringSoftReference = new SoftReference<User>(daiyanping);
        // 将强引用清除
        daiyanping = null;
        System.gc();
        System.out.println("gc后查看是否存在" + stringSoftReference.get());
        ArrayList<byte[]> bytes = new ArrayList<>();
        try {
            for (int i = 0; i < 20; i++) {
                bytes.add(new byte[1024 * 1024]);

            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("OOM后查看软引用是否存在" + stringSoftReference.get());
        }
    }

    public static void test2() {
        String sdfds = new String("sfds");
        SoftReference<String> stringSoftReference = new SoftReference<String>(sdfds);
        // 将强引用清除
        sdfds = null;
        System.gc();
        System.out.println("gc后查看是否存在" + stringSoftReference.get());
        ArrayList<byte[]> bytes = new ArrayList<>();
        try {
            for (int i = 0; i < 20; i++) {
                bytes.add(new byte[1024 * 1024]);

            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("OOM后查看软引用是否存在" + stringSoftReference.get());
        }
    }

    public static void test3() {
        String sdfds = "sdfsf";
        SoftReference<String> stringSoftReference = new SoftReference<String>(sdfds);
        // 将强引用清除
        sdfds = null;
        System.gc();
        System.out.println("gc后查看是否存在" + stringSoftReference.get());
        ArrayList<byte[]> bytes = new ArrayList<>();
        try {
            for (int i = 0; i < 20; i++) {
                bytes.add(new byte[1024 * 1024]);

            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("OOM后查看软引用是否存在" + stringSoftReference.get());
        }
    }

    @AllArgsConstructor
    static class User {
        private String name;

        private String password;
    }
}
