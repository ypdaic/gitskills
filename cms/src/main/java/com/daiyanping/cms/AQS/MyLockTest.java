package com.daiyanping.cms.AQS;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName MyLockTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-25
 * @Version 0.1
 */
public class MyLockTest {

    private static MyLock myLock = new MyLock();

    private static int count;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {

            Thread thread = new Thread(() -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myLock.lock();
                try {
                    count++;
                    System.out.println(count);
//                    myLock.lock();
//                    try {
//                        count++;
//                        System.out.println(count);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        myLock.unlock();
//                    }
//                    Thread.sleep(1000 * 20);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    myLock.unlock();
                }
            });
            thread.start();
            if (i == 1) {
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                thread.interrupt();
            }
        }
    }
}
