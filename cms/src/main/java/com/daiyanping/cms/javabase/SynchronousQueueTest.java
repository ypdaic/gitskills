package com.daiyanping.cms.javabase;

import java.util.concurrent.SynchronousQueue;

/**
 * @ClassName SynchronousQueueTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-04
 * @Version 0.1
 */
public class SynchronousQueueTest {

    public static void main(String[] args) {
        SynchronousQueue<String> strings = new SynchronousQueue<>();
        strings.offer("ss");
        try {
            strings.take();
        } catch (InterruptedException e) {


        }
    }
}
