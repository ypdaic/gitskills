package com.daiyanping.cms.concurrent.test;

/**
 * sync是否为公平锁
 */
public class TestSysn {
    //定义一把锁
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        //线程的数量
        int N = 10;
        Thread[] threads = new Thread[N];


        for(int i = 0; i < N; ++i){
            threads[i] = new Thread(new Runnable(){
                public void run() {
                    /**
                     * 如果这里打印的结果是无序的则表示 非公平锁
                     * 有序则公平锁
                     * 倒叙 为什么几乎上不可能研究
                     * 因为他存在一个队列  C++
                     */
                    synchronized(lock){//t0  1.6 mutext---t0 t1....t9 到一个队列当中的去阻塞
                        System.out.println(Thread.currentThread().getName() + " get synch lock!");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            });
        }

        //main 线程可以得到锁 持有了锁
        synchronized(lock){
            for(int i = 0; i < N; ++i){
                //t0
                threads[i].start();
                Thread.sleep(200);
            }
        }
//Thread.sleep(200);
//        for(int i = 0; i < N; ++i)
//            threads[i].join();
        Thread.sleep(200);
        for(int i = 0; i < N; ++i){
            new Thread(new Runnable(){
                public void run() {
                    /**
                     * 如果这里打印的结果是无序的则表示 非公平锁
                     * 有序则公平锁
                     * 倒叙 为什么几乎上不可能研究
                     * 因为他存在一个队列  C++
                     */
                    synchronized(lock){//t0  1.6 mutext---t0 t1....t9 到一个队列当中的去阻塞
                        System.out.println(Thread.currentThread().getName() + " get synch lock again!");
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
                    }
                }

            }).start();
            Thread.sleep(197);
        }

    }
}
