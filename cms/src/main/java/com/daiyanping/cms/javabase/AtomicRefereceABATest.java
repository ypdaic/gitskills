package com.daiyanping.cms.javabase;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 原子引用，存在ABA问题
 */
public class AtomicRefereceABATest {

    public static void main(String[] args) {
        System.out.println("---------------------存在ABA问题-----------------------------");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        new Thread(() -> {
            atomicInteger.compareAndSet(0, 100);
            atomicInteger.compareAndSet(100, 0);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean b = atomicInteger.compareAndSet(0, 100);
            System.out.println("是否修改成功" + b + "值：" + atomicInteger.get());
        }).start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("----------------------解决ABA问题-----------------------");
        /**
         * 版本号比较就是比较AtomicStampedReference内部的Pair的stamp
         */
        AtomicStampedReference<Integer> integerAtomicStampedReference = new AtomicStampedReference<Integer>(1, 1);

        new Thread(() -> {
            int stamp = integerAtomicStampedReference.getStamp();
            System.out.println("线程t3第一次获取版本号:" + stamp);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            integerAtomicStampedReference.compareAndSet(1, 2, stamp, stamp + 1);
            int stamp1 = integerAtomicStampedReference.getStamp();
            System.out.println("线程t3第二次获取版本号:" + stamp1);

            integerAtomicStampedReference.compareAndSet(2, 1, stamp1, stamp1 + 1);

        }, "t3").start();

        new Thread(() -> {
            int stamp = integerAtomicStampedReference.getStamp();
            System.out.println("线程t4第一次获取版本号:" + stamp);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean b = integerAtomicStampedReference.compareAndSet(1, 2, stamp, stamp + 1);
            System.out.println("线程t4修改的结果" + b + "当前版本号:" + integerAtomicStampedReference.getStamp());
        }).start();



    }

    @Data
    public static class User {
        private String name;
    }
}
