package com.daiyanping.cms.javabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class HashSetNoSafeTest {

    public static void main(String[] args) {
        /**
         * 解决方案
         * new Vector
         * Collections.synchronizedList
         * CopyOnWriteArrayList
         */
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                // 这里会走new HashIterator(); 获取获取当前的modCount，而HashSet在修改，就会导致modCount和HashIterator ()的expectedModCount不相等
                // 导致抛出ConcurrentModificationException 异常
                System.out.println(set);
            }).start();

        }
    }
}
