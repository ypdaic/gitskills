package com.daiyanping.cms.javabase;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 */
public class ReadWriteLockTest {

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();

    private HashMap hashMap = new HashMap();

    public static void main(String[] args) {
        ReadWriteLockTest readWriteLockTest = new ReadWriteLockTest();
        for (int i = 0; i < 10; i++) {
            int temInt = i;
            new Thread(() -> {
                readWriteLockTest.put(temInt + "", temInt + "");
            }, i + "").start();

        }

        for (int i = 0; i < 10; i++) {
            int temInt = i;
            new Thread(() -> {
                readWriteLockTest.get(temInt + "");
            }, i + "").start();

        }
    }

    // 写写互斥，读写互斥
    public void put(String key, String value) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "正在写入" + key);

            hashMap.put(key, value);

            System.out.println(Thread.currentThread().getName() + "正在写入完成" + key);
        } finally {
            writeLock.unlock();
        }


    }

    // 读读不互斥
    public void get(String key) {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "正在读取" + key);

            hashMap.get(key);

            System.out.println(Thread.currentThread().getName() + "正在读取完成" + key);
        } finally {
            readLock.unlock();
        }


    }


}
