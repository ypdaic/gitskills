package com.daiyanping.cms.jvm.OOM;

import java.util.concurrent.CountDownLatch;

/**
 * 高并发请求服务器时，经常出现如下异常 java.lang.OutOfMemoryError:unable to create new native thread
 * 准确的讲该native thread 异常与对应的平台有关
 *
 * 导致原因：
 *
 *  1 你的应用创建了太多的线程了，一个应用进程创建多个线程，超过系统承载极限
 *  2 你的服务器并不允许你的应用程序创建了这么多线程，liunx系统默认运行单个进程可以创建的线程数是1024个
 *    你的应用创建超过这个数量，就会报java.lang.OutOfMemoryError:unable to create new native thread
 *
 * 解决办法：
 * 1：想办法降低你应用程序创建线程的数量，分析应用是否真的需要创建这么多线程，如果不是，改代码将线程数降到最低
 * 2：对于有的应用，确实需要创建很多的线程，远超过linux系统的默认1024个线程的限制，可以通过修改liunx服务器配置，扩大linux默认限制
 *
 *
 * linux 查询用户可以场景的线程数
 * ulimit -u
 *
 *  cd /etc/security/limits.d
 */
public class UnableToCreateThreadError {

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            new Thread(() -> {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }
}
