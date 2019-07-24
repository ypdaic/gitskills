package com.daiyanping.cms.ThreadLocal;

import java.util.concurrent.*;

/**
 * @ClassName ThreadLocalOOM
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-19
 * @Version 0.1
 */
public class ThreadLocalOOM {

    private static ExecutorService executorService = new ThreadPoolExecutor(5, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    static class Test {
        private byte[] value = new byte[30 * 1024 * 1024];
    }

    // 存在强引用，不会被回收
    private static ThreadLocal<Test> threadLocal = new ThreadLocal();

    public static void main(String[] args) {

        new Thread(() -> {
            while (true) {
                  // 只存在弱引用，当threadLocal被回收后，又不调用remove方法，会导致内存泄漏，所以这种情况下必须调用remove方法
                  ThreadLocal<Test> threadLocal = new ThreadLocal();
                  threadLocal.set(new Test());
//                  threadLocal.remove();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                // 存在强引用，threadLocal不会被回收，只不过是新值替换旧值，不remove也没有问题，一般还是要调用remove 方法
                threadLocal.set(new Test());
//                  threadLocal.remove();
            }
        }).start();


    }
}
