package com.daiyanping.cms.javabase.problem;

public class DeadlockTest {

    private static Object object = new Object();

    private static Object object2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (object) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object2) {

                }
            }
        }).start();

        new Thread(() -> {
            synchronized (object2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object) {

                }
            }
        }).start();

    }
}
