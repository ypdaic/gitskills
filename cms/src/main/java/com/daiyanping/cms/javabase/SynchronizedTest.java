package com.daiyanping.cms.javabase;

/**
 * @ClassName SynchronizedTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-25
 * @Version 0.1
 */
public class SynchronizedTest {

    /**
     * 验证线程阻塞在synchronized上时的状态是BLOCKED
     *
     * 名称: Thread-2
     * 状态: java.lang.Object@4d12b1ba上的BLOCKED, 拥有者: Thread-1
     * 总阻止数: 1, 总等待数: 0
     *
     * 堆栈跟踪:
     * com.daiyanping.cms.javabase.SynchronizedTest.lambda$main$1(SynchronizedTest.java:33)
     * com.daiyanping.cms.javabase.SynchronizedTest$$Lambda$2/2025864991.run(Unknown Source)
     * java.lang.Thread.run(Thread.java:748)
     *
     * 验证线程在wait方法等待时状态是TIMED_WAITING
     *
     * 名称: Thread-1
     * 状态: TIMED_WAITING
     * 总阻止数: 0, 总等待数: 1
     *
     * 堆栈跟踪:
     * java.lang.Thread.sleep(Native Method)
     * com.daiyanping.cms.javabase.SynchronizedTest.lambda$main$0(SynchronizedTest.java:33)
     *    - 已锁定 java.lang.Object@447bc0f5
     * com.daiyanping.cms.javabase.SynchronizedTest$$Lambda$1/274064559.run(Unknown Source)
     * java.lang.Thread.run(Thread.java:748)
     * @param args
     */
    public static void main(String[] args) {
        Object o = new Object();
        new Thread(() -> {
            Thread.currentThread().setName("Thread-1");
            synchronized (o) {
                try {
                    Thread.sleep(10000 * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            Thread.currentThread().setName("Thread-2");
            synchronized (o) {
                try {
                    Thread.sleep(10000 * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
