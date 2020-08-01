package com.daiyanping.cms.javabase;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapSafeTest {


    /**
     * 不安全，一样会有ConcurrentModificationException异常出现
     * @param args
     */
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                map.put(UUID.randomUUID().toString().substring(0, 8), "a");
                // 由于table 是volatile的其他线程变更，可以立马感知到，所有查询并不需要进行同步
                System.out.println(map);
            }).start();

        }
    }
}
