package com.daiyanping.cms.javabase;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @ClassName PriorityBlockingQueueTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-04
 * @Version 0.1
 */
public class PriorityBlockingQueueTest {

    /**
     * 按优先级排序的无界阻塞队列
     * @param args
     */
    public static void main(String[] args) {
        PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue();
        priorityBlockingQueue.offer("ss");
        priorityBlockingQueue.offer("ssd");
        try {
            priorityBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
