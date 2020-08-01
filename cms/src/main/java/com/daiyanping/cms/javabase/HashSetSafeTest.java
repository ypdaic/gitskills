package com.daiyanping.cms.javabase;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class HashSetSafeTest {

    public static void main(String[] args) {
        /**
         * 内部还是使用CopyOnWriteArrayList，如果添加过该元素，就不走
         * addIfAbsent
         */
        CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                // 这里add会加锁，而get方法不用加锁
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }).start();

        }
    }
}
