package com.daiyanping.cms.javabase;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayListSafeTest {

    /**
     * 写时复制，只是写加锁，读不加锁，比都加锁要快
     * @param args
     */
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                // 这里add会加锁，而get方法不用加锁
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }).start();

        }
    }
}
