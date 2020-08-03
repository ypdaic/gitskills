package com.daiyanping.cms.javabase.string;

import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.CountDownLatch;

/**
 * 使用intern()方法测试执行效率：空间使用上
 * 结论：对于程序中大量存在的字符串，尤其其中存在很多重复字符串时，使用intern() 可以节省内存空间
 */
public class StringIntern2Test {

    static final int MAX_COUNT = 1000 * 10000;

    static final String[] arr = new String[MAX_COUNT];

    /**
     * 使用了intern方法字符串常量池信息
     * StringTable statistics:
     * Number of buckets       :     60013 =    480104 bytes, avg   8.000
     * Number of entries       :      3200 =     76800 bytes, avg  24.000
     * Number of literals      :      3200 =    262104 bytes, avg  81.907
     * Total footprint         :           =    819008 bytes
     * Average bucket size     :     0.053
     * Variance of bucket size :     0.054
     * Std. dev. of bucket size:     0.232
     * Maximum bucket size     :         2
     *
     *
     * 没有使用intern方法，字符串常量池信息
     * StringTable statistics:
     * Number of buckets       :     60013 =    480104 bytes, avg   8.000
     * Number of entries       :      3188 =     76512 bytes, avg  24.000
     * Number of literals      :      3188 =    261456 bytes, avg  82.013
     * Total footprint         :           =    818072 bytes
     * Average bucket size     :     0.053
     * Variance of bucket size :     0.054
     * Std. dev. of bucket size:     0.231
     * Maximum bucket size     :         2
     * @param args
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Integer[] integers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int i = 0; i < MAX_COUNT; i++) {
            // 这种情况arr存的字符串常量池中字符串
//            String intern = new String(String.valueOf(integers[i % integers.length])).intern();
            String intern = new String(String.valueOf(integers[i % integers.length]));

            // 两者字符串的个数明显不一样
            arr[i] = intern;


        }

        long endTime = System.currentTimeMillis();
        System.out.println("花费的时间" + (endTime - startTime));

//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}


