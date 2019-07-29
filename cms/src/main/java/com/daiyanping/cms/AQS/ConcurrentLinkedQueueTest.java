package com.daiyanping.cms.AQS;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName ConcurrentLinkedQueueTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-29
 * @Version 0.1
 */
public class ConcurrentLinkedQueueTest {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<Integer> integers = new ConcurrentLinkedQueue<>();
        integers.offer(1);
        integers.offer(2);
    }
}
