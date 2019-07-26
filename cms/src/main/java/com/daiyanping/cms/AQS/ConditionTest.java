package com.daiyanping.cms.AQS;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ConditionTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-26
 * @Version 0.1
 */
public class ConditionTest {

    private final static ReentrantLock reentrantLock = new ReentrantLock();

    private final static Condition condition = reentrantLock.newCondition();


    public static void main(String[] args) {
//        test1();
        test2();

    }

    /**
     * condition 在阻塞期间，如果线程被中断过，线程释放后，就会抛出中断异常
     */
    public static void test1() {
        Thread thread = new Thread(() -> {
            reentrantLock.lock();
            try {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }
        });
        thread.start();


        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    /**
     * signal方法释放后，会将await的节点放到同步队列队尾，如果前面存在正在等待节点，那就必须等前面的节点正常释放锁，才能轮到await节点，
     * 以保障先进先出
     */
    public static void test2() {
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("thread1");
            reentrantLock.lock();
            try {
                try {
                    System.out.println("线程1等待");
                    condition.await();
                    System.out.println("线程1等待后释放");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }
        });
        thread.start();

        Thread thread2 = new Thread(() -> {
            Thread.currentThread().setName("thread2");
            reentrantLock.lock();
            try {
                try {
                    System.out.println("线程2等待");
                    condition.await();
                    System.out.println("线程2等待后释放");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }
        });
        thread2.start();



        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread3 = new Thread(() -> {
            Thread.currentThread().setName("thread3");
            reentrantLock.lock();
            try {
                System.out.println("线程3抢到锁");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                condition.signal();

            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }
        });
        thread3.start();

        Thread thread4 = new Thread(() -> {
            Thread.currentThread().setName("thread4");
            reentrantLock.lock();
            try {
                System.out.println("线程4抢到锁");
                condition.signal();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }
        });
        thread4.start();


    }
}
