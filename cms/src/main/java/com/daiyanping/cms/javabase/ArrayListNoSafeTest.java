package com.daiyanping.cms.javabase;

import java.util.ArrayList;
import java.util.UUID;

public class ArrayListNoSafeTest {

    /**
     * ArrayList 线程不安全测试
     * 当ArrayList在修改时，此时访问，可能会出现ConcurrentModificationException异常
     * @param args
     */
    public static void main(String[] args) {

        /**
         * 解决方案
         * new Vector
         * Collections.synchronizedList
         * CopyOnWriteArrayList
         */
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                // 这里会走new Itr(); 获取获取当前的modCount，而arrayList在修改，就会导致modCount和Itr()的expectedModCount不相等
                // 导致抛出ConcurrentModificationException 异常
                System.out.println(list);
            }).start();

        }
    }
}
