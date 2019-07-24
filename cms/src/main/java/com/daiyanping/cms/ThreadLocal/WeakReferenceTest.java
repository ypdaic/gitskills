package com.daiyanping.cms.ThreadLocal;

import java.lang.ref.WeakReference;

/**
 * @ClassName WeakReferenceTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-19
 * @Version 0.1
 */
public class WeakReferenceTest {

    private static String test = "hello";

    private static ThreadLocal<String> threadLocal = new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return "hello";
        }
    };


    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();

    }

    /**
     * threadLocal只存在弱引用，gc后，就会被回收
     */
    public static void test1() {

        WeakReference<ThreadLocal<String>> sr = new WeakReference<ThreadLocal<String>>(new ThreadLocal(){
            @Override
            protected Object initialValue() {
                return "hello";
            }
        });

        System.out.println(sr.get());
        System.gc();                //通知JVM的gc进行垃圾回收
        System.out.println(sr.get());
    }

    /**
     * threadLocal存在static的强引用，gc后，不会被回收
     */
    public static void test2() {

        WeakReference<ThreadLocal<String>> sr = new WeakReference<ThreadLocal<String>>(threadLocal);

        System.out.println(sr.get());
        System.gc();                //通知JVM的gc进行垃圾回收
        System.out.println(sr.get());
    }


    public static void test3() {
        // initialValue() 方法会在使用get方法返回null的threadLocalMap时调用
        ThreadLocal<String> threadLocal = new ThreadLocal(){
            @Override
            protected Object initialValue() {
                return "hello";
            }
        };

        System.out.println(threadLocal.get());
    }

    public static void test4() {

        WeakReference<String> sr = new WeakReference<String>(test);

        System.out.println(sr.get());
        System.gc();                //通知JVM的gc进行垃圾回收
        System.out.println(sr.get());
    }

}
