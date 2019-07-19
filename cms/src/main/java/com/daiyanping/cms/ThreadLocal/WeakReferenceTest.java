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


//    public void main(String[] args) {
//
//        WeakReference<String> sr = new WeakReference<String>(test);
//
//        System.out.println(sr.get());
//        System.gc();                //通知JVM的gc进行垃圾回收
//        System.out.println(sr.get());
//    }

    public static void main(String[] args) {

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

//    public static void main(String[] args) {
//
//        WeakReference<ThreadLocal<String>> sr = new WeakReference<ThreadLocal<String>>(threadLocal);
//
//        System.out.println(sr.get());
//        System.gc();                //通知JVM的gc进行垃圾回收
//        System.out.println(sr.get());
//    }

//    public static void main(String[] args) {
//
//        ThreadLocal<String> threadLocal = new ThreadLocal(){
//            @Override
//            protected Object initialValue() {
//                return "hello";
//            }
//        };
//
//        System.out.println(threadLocal.get());
//    }
//
//    public static void test(ThreadLocal<String> threadLocal) {
//        threadLocal = null;
//    }


}
