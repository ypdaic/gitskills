package com.daiyanping.cms.jvm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName GCTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/7/29
 * @Version 0.1
 */
public class GCTest {

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    /**
     * gc 性能优化
     * -XX:+PrintGCDetails
     * -XX:+PrintGCDateStamps
     * -XX:+PrintGCTimeStamps
     * -XX:+UseConcMarkSweepGC
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        executorService.scheduleAtFixedRate(new Producer(200 * 1024 * 1024 / 1000, 5), 0, 100, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(new Producer(50 * 1024 * 1024 / 1000, 120), 0, 100, TimeUnit.MILLISECONDS);
        TimeUnit.MINUTES.sleep(10);
        executorService.shutdownNow();
    }


    //imports skipped for brevity
    public static class Producer implements Runnable {


        private Deque<byte[]> deque;
        private int objectSize;
        private int queueSize;

        public Producer(int objectSize, int ttl) {
            this.deque = new ArrayDeque<byte[]>();
            this.objectSize = objectSize;
            this.queueSize = ttl * 1000;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                deque.add(new byte[objectSize]);
                if (deque.size() > queueSize) {
                    deque.poll();
                }
            }
        }

    }
}
