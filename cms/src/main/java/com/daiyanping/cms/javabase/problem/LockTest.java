package com.daiyanping.cms.javabase.problem;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                shareData.incremment();
            }).start();

            new Thread(() -> {
                shareData.decrement();
            }).start();
            new Thread(() -> {
                shareData.incremment();
            }).start();
            new Thread(() -> {
                shareData.decrement();
            }).start();

        }
    }

    public static class ShareData {

        private int value = 0;

        private ReentrantLock lock = new ReentrantLock();

        private Condition condition = lock.newCondition();

        public void incremment() {
            lock.lock();
            try {
                while (value != 0) {
                    condition.await();
                }
                value++;
                System.out.println(value);
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }




        }

        public void decrement() {
            lock.lock();

            try {
                while (value == 0) {
                    condition.await();
                }
                value--;
                System.out.println(value);
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }


        }
    }
}
