package com.daiyanping.cms.AQS;

public class ThreadTest {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.run();
    }

    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("xxxx");
        }
    }
}
