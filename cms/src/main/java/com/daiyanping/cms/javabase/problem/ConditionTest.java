package com.daiyanping.cms.javabase.problem;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    /**
     * A 打印 5次
     * B 打印 10次
     * C 打印 15 次
     * 打印10 轮
     * @param args
     */
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print5();
            }

        }, "a").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print10();
            }

        }, "b").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print15();
            }

        }, "c").start();

    }

    public static class ShareData {

        // 1 a 2 b 3 c
        private int number = 1;

        private ReentrantLock lock = new ReentrantLock();

        private Condition conditionA = lock.newCondition();

        private Condition conditionB = lock.newCondition();

        private Condition conditionC = lock.newCondition();

        public void print5() {
            lock.lock();
            try {
                while (number != 1) {
                    conditionA.await();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + i);

                }
                number = 2;
                conditionB.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }




        }

        public void print10() {
            lock.lock();
            try {
                while (number != 2) {
                    conditionB.await();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + i);

                }
                number = 3;
                conditionC.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }




        }

        public void print15() {
            lock.lock();
            try {
                while (number != 3) {
                    conditionC.await();
                }
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName() + i);

                }
                number = 1;
                conditionA.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }




        }

    }

}
