package com.daiyanping.cms.javabase.problem;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SyncTest {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                shareData.incremment();
            }).start();

            new Thread(() -> {
                shareData.decrement();
            }).start();
//            new Thread(() -> {
//                shareData.incremment();
//            }).start();
//            new Thread(() -> {
//                shareData.decrement();
//            }).start();

        }
    }

    public static class ShareData {

        private int value = 0;

        private Object lock = new Object();

        public void incremment() {
            synchronized (lock) {
                while (value != 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                value++;
                System.out.println(value);
                lock.notifyAll();
            }




        }

        public void decrement() {
            synchronized (lock) {
                while (value == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                value--;
                System.out.println(value);
                lock.notifyAll();
            }



        }
    }
}
