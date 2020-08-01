package com.daiyanping.cms.javabase;

import java.util.concurrent.CountDownLatch;

public class VolatileReorderTest {

    public static void main(String[] args) {
        MyData myData = new MyData();

        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
//            countDownLatch.countDown();
//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            myData.test1();
        }).start();
        new Thread(() -> {
//            countDownLatch.countDown();
//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            myData.test2();
        }).start();
    }

    public static class MyData {

        private int a = 0;

        private boolean f = false;

        public void test1() {
            a = 1;
            f = true;
        }

        /**
         * 由于存在指令重排，这里的a 可能是5
         */
        public void test2() {
            if (f) {
                a = a + 5;
                System.out.println("test2的结果" + a);
            }
        }
    }
}
