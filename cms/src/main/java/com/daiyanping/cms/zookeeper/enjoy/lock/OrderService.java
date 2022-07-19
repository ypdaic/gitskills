package com.daiyanping.cms.zookeeper.enjoy.lock;

import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.concurrent.CountDownLatch;

/**
 * @Classname OrderService
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 20:12
 * Version 1.0
 */
public class OrderService implements Runnable {

    private OrderNumFactory orderNumFactory = new OrderNumFactory();

    private static Integer count = 50;

    private static CountDownLatch cdl = new CountDownLatch(count);

    private Lock lock = new ZkImproveLockImpl();

    @Override
    public void run() {
        try {
            //线程运行起来第一件事情就是等待
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createOrderNum();
    }

    private void createOrderNum() {
        lock.lock();
        String orderNum = orderNumFactory.createOrderNum();
        System.out.println(Thread.currentThread().getName() + "创建了订单号：[" + orderNum + "]");
        lock.unlock();
    }

    public static void main(String[] args) {
        for (int i = 0; i < count; i++) {
            new Thread(new OrderService()).start();
            cdl.countDown();
        }
    }
}
