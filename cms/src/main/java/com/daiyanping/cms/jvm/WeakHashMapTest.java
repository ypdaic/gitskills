package com.daiyanping.cms.jvm;

import java.util.WeakHashMap;

public class WeakHashMapTest {

    /**
     * weakhashmap 使用, key 必须为new 出来的对象
     * @param args
     */
    public static void main(String[] args) {
        WeakHashMap<String, String> stringStringWeakHashMap = new WeakHashMap<>();
        String key = new String("2");
        stringStringWeakHashMap.put(key, "sss");
        System.out.println(stringStringWeakHashMap);
        key = null;
        System.gc();

        System.out.println(stringStringWeakHashMap);
    }
}
