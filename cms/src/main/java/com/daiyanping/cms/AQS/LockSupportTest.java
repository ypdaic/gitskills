package com.daiyanping.cms.AQS;

import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName LockSupportTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-26
 * @Version 0.1
 */
public class LockSupportTest {

    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("开始阻塞");

                // park方法可以响应中断，但不会抛中断异常
                LockSupport.park(o);
                System.out.println("响应中断");

                // 清除中断标志位
                Thread.interrupted();
            }

        });

        thread.start();

        Thread.sleep(1000 * 10);

        thread.interrupt();

    }
}
