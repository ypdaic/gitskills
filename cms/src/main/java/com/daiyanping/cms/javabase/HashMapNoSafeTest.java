package com.daiyanping.cms.javabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class HashMapNoSafeTest {


    /**
     * 不安全，一样会有ConcurrentModificationException异常出现
     * @param args
     */
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                map.put(UUID.randomUUID().toString().substring(0, 8), "a");
                // 这里会走new HashIterator(); 获取获取当前的modCount，而HashSet在修改，就会导致modCount和HashIterator ()的expectedModCount不相等
                // 导致抛出ConcurrentModificationException 异常
                System.out.println(map);
            }).start();

        }
    }
}
