package com.daiyanping.cms.jvm;

import java.util.concurrent.CountDownLatch;

public class SyncTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
