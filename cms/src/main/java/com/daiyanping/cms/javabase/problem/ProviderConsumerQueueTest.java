package com.daiyanping.cms.javabase.problem;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProviderConsumerQueueTest {

    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue(10));
        new Thread(() -> {
            myResource.provide();
        }, "Prod").start();

        new Thread(() -> {
            myResource.consumer();
        }, "Consumer").start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {


        }
        myResource.stop();
        System.out.println("主线程叫停");
    }

   public static class MyResource {

       private volatile boolean flag = true;

       private AtomicInteger atomicInteger = new AtomicInteger(0);

       private BlockingQueue<String> blockingQueue = null;

       public MyResource(BlockingQueue blockingQueue) {
           this.blockingQueue = blockingQueue;
       }

       public void provide() {
           String data = null;
           while (flag) {
               data = atomicInteger.incrementAndGet() + "";
               blockingQueue.offer(data);
               System.out.println(Thread.currentThread().getName() + "插入队列成功" + data);
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }

           System.out.println("生成者退出");
       }

       public void consumer() {
           String data = null;
           while (flag) {

               try {
                    data = blockingQueue.poll(2, TimeUnit.SECONDS);
                    if (StringUtils.isEmpty(data)) {
                        System.out.println("消费者退出");
                        return;
                    }
                    System.out.println(Thread.currentThread().getName() + "消费队列成功" + data);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }

           System.out.println("消费者退出");
       }

       public void stop() {
           flag = false;
       }


   }
}
