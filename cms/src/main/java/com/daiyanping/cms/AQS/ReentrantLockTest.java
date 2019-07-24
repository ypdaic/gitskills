package com.daiyanping.cms.AQS;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ReentrantLockTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class ReentrantLockTest {

    private static ReentrantLock reentrantLock = new ReentrantLock();

    private static Condition condition = reentrantLock.newCondition();

    /**
     * 测试锁重入后，在condition的await释放后，后面的unlock是否会出现问题
     * 测试结果是，没有问题，应为在await方法等待释放后，会重新获取锁，也就是重新设置信号量的值，该值就不是1了，而是await前当前线程所持有的信号量
     * @param args
     */
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            reentrantLock.lock();
            try {
                reentrantLock.lock();
                try {
                    System.out.println("等待释放");
                    condition.await();
                    System.out.println("释放成功");

                } catch (Exception e) {

                } finally {
                    reentrantLock.unlock();
                }


            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }

        });

        Thread thread2 = new Thread(() -> {
            // yield方法 表示当前线程让出cpu执行权，也有可能让出后，又会立马分配到cpu
            Thread.yield();
            reentrantLock.lock();
            try {
                condition.signal();
                System.out.println("释放信号量");

            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }

        });

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
