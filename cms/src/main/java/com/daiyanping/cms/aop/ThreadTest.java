package com.daiyanping.cms.aop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadTest {

    public static void main(String[] args) {
        test(1);
    }

    public static void test(int n) {
        List<CountDownLatch> countDownLatchArrayList = new ArrayList<CountDownLatch>();
        for (int i = 1; i <= n; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            countDownLatchArrayList.add(countDownLatch);
            final int num = i;
            Thread thread = new Thread(() -> {
                try {
                    countDownLatch.await();
                    if (num < n) {

                        System.out.print(num + ",");
                    } else {
                        System.out.print(num);
                    }
                    if (num < n) {
                        CountDownLatch countDownLatchNext = countDownLatchArrayList.get(num);
                        countDownLatchNext.countDown();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        CountDownLatch countDownLatch = countDownLatchArrayList.get(0);
        countDownLatch.countDown();
    }
}
