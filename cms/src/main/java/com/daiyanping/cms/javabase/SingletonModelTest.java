package com.daiyanping.cms.javabase;

import lombok.Data;

import java.util.concurrent.CountDownLatch;

public class SingletonModelTest {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Test instance = Test.getInstance();
                if (instance.test2.getName() == null) {
                    System.out.println(Thread.currentThread().getName());
                }
            }, i + "").start();

        }
    }

    /**
     * 由于存在指令重排序，可能会出现test2还没有创建完成就已经被使用了
     * 这里就需要使用volatile修饰
     */
    public static class Test {

        private static volatile Test instance = null;

        private Test2 test2;

        private Test() {
            this.test2 = new Test2();
            test2.setName("aaa");
            test2.setPassword("dddd");
        }

        public static Test getInstance() {
            if (instance == null) {
                synchronized (Test.class) {
                    if (instance == null) {
                        instance = new Test();
                    }
                }
            }

            return instance;
        }
    }

    @Data
    public static class Test2 {

        private String name;

        private String password;
    }
}
