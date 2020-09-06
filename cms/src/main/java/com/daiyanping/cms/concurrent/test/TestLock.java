package com.daiyanping.cms.concurrent.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 这是一把公平锁
 * 公平锁非公平锁理解本身就错了
 *
 *
 * 非公平锁他们首先会在lock方法调用加锁的时候去抢锁（公平锁调用lock不会上来就拿锁）；
 * 如果加锁失败
 * 则去看为什么失败（是否锁被人持有了），他在判断的时候如果锁没有被人持有
 * 非公平锁有会去直接加锁（不会判断是否有人排队），成功则进入同步块；失败则进入队列
 * 进入队列后如果前面那个人是head则再次尝试加锁，成功则执行同步代码块，失败则park（真正的排
 队）
 *
 * 公平锁：第一次加锁的时候，他不会去尝试加锁，他会去看一下我前面有没有人排队，如果有人排队，我
 则进入
 * 队列（并不等于排队），然后还不死心，再次看一下我有没有拿锁的资格（前面那个人是否为head），
 * 如果有资格（前面那个人刚好为head）则继续拿锁，成功则执行同步块；失败则park（排队）
 *
 *
 * 一朝排队，永远排队
 *
 结果分析：lock实现的同步如果有多个线程阻塞，唤醒的时候是按阻塞顺序唤醒的；关于公平锁和非公
 平锁可以参数我写的注释
 */
public class TestLock {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        int N = 10;
        Thread[] threads = new Thread[N];
        for(int i = 0; i < N; ++i){
            threads[i] = new Thread(new Runnable(){
                public void run() {
                    //synchronized ()
                    /**
                     *
                     * 独占锁---顾名思义
                     * t1------t9全部在这里阻塞
                     * 非公平锁也是拿不到锁---阻塞---进入队列---
                     * 线程
                     */
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " lock!");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    lock.unlock();
                }

            });
        }
        //synchronized ()
        lock.lock();
        for(int i = 0; i < N; ++i){
            threads[i].start();
            Thread.sleep(200);
        }
        lock.unlock();

//        for(int i = 0; i < N; ++i)
//            threads[i].join();
        Thread.sleep(200);
        for(int i = 0; i < N; ++i){
            new Thread(new Runnable(){
                public void run() {
                    //synchronized ()
                    /**
                     *
                     * 独占锁---顾名思义
                     * t1------t9全部在这里阻塞
                     * 非公平锁也是拿不到锁---阻塞---进入队列---
                     * 线程
                     */
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " lock!");
//                    try {
//                        Thread.sleep(20);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
                    lock.unlock();
                }

            }).start();
            Thread.sleep(200);
        }
    }
}
