package com.daiyanping.cms.AQS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadPoolExecutorTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-29
 * @Version 0.1
 */
public class ThreadPoolExecutorTest {

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        test2();
//        test3();
//        test4();
//        test6();
//        test();
    }

    /**
     * shutdown()方法，会将线程池的运行状态设置为SHUTDOWN，SHUTDOWN状态的线程池，不能添加新任务。shutdown
     * 只会中断空闲的work线程，正在执行的业务的线程无法被中断。shutdown 后，work线程会执行完当前任务，和
     * 列队中的任务后，work线程才会退出。
     *
     */
    public static void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                System.out.println("ssss");
//                try {
//                    Thread.sleep(1000 * 10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("sdfsf");
            });
        }

//        try {
//            Thread.sleep(1000 * 5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        executorService.shutdown();

//        executorService.execute(()-> {
//            System.out.println("sdfs");
//        });
    }

    /**
     * shutdownNow()方法，会将线程池的运行状态设置为STOP，STOP状态的线程池，不能添加新任务
     * 且会移除队列中所有未执行的任务，并中断work线程，work线程执行完业务后就会退出
     */
    public static void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                System.out.println("ssss");
//                try {
//                    Thread.sleep(1000 * 10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
////                    Thread.interrupted();
//                }
            });
        }

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        executorService.shutdownNow();
//        executorService.execute(()-> {
//            System.out.println("sdfs");
//        });
    }

    /**
     * return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                                       60L, TimeUnit.SECONDS,
     *                                       new SynchronousQueue<Runnable>());
     * 其工作原理就是，核心线程数为0，所以先就入队列，而队列又是SynchronousQueue，第一次入队列失败，立马添加一个work进行处理，
     * 如果在work线程获取任务时，添加任务，此时添加的任务
     * 会被该work消化，否则创建新的work进行处理，如果某个work在keepAliveTime时间获取不到任务，该work就会退出。
     * 该work退出后，不会再次创建work（总结就是没有空闲的work，就创建新的work，空闲work的的空闲时间为60秒，所有的work都获取不到任务，则线程池没有任何work运行）
     */
    public static void test3() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i++) {
            executorService.execute(()->{
                System.out.println("ssss");
                try {
                    Thread.sleep(1000 * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    Thread.interrupted();
                }
            });
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        executorService.shutdownNow();
//        executorService.execute(()-> {
//            System.out.println("sdfs");
//        });
    }

    /**
     * public static ExecutorService newSingleThreadExecutor() {
     *         return new FinalizableDelegatedExecutorService
     *             (new ThreadPoolExecutor(1, 1,
     *                                     0L, TimeUnit.MILLISECONDS,
     *                                     new LinkedBlockingQueue<Runnable>()));
     *     }
     *
     * 其工作原理就是，核心线程数，最大线程为1，那么提交的所有任务都会被这一个work线程进行处理
     * work获取任务的流程就是如果当前Work小于核心线程数，使用队列的take方法取任务，否则使用队列的poll方法，并指定超时时间，该
     * 超时时间就是我们的keepAliveTime参数，所以这个参数就是给超过核心线程数的work使用的，当该work 获取任务超时，则会退出执行
     * 退出后，如果当前work数大于核心线程数则不做任何处理，否则重新创建一个work
     *
     *
     */
    public static void test4() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 1; i++) {
            executorService.execute(()->{
                System.out.println("ssss");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    Thread.interrupted();
                }
            });
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        executorService.shutdownNow();
//        executorService.execute(()-> {
//            System.out.println("sdfs");
//        });
    }

    /**
     * public static ExecutorService newFixedThreadPool(int nThreads) {
     *         return new ThreadPoolExecutor(nThreads, nThreads,
     *                                       0L, TimeUnit.MILLISECONDS,
     *                                       new LinkedBlockingQueue<Runnable>());
     *     }
     *
     * 其工作原理就是，核心线程数，最大线程数相等，也就是一个固定线程数的线程池，多余的任务全部
     * 丢到队列中
     *
     *
     */
    public static void test5() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 1; i++) {
            executorService.execute(()->{
                System.out.println("ssss");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    Thread.interrupted();
                }
            });
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        executorService.shutdownNow();
//        executorService.execute(()-> {
//            System.out.println("sdfs");
//        });
    }

    /**
     * public ScheduledThreadPoolExecutor(int corePoolSize) {
     *         super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
     *               new DelayedWorkQueue());
     *     }
     *
     * ScheduledThreadPoolExecutor其工作原理：核心线程数指定，最大线程数int 最大值，不可指定，队列使用DelayedWorkQueue
     * DelayedWorkQueue内部使用数组保存，初始化大小为16，当元素个数大于等于16时进行扩容，每次扩容50%
     * 往DelayedWorkQueue添加ScheduledFutureTask时，都要和上一个ScheduledFutureTask比较其延时时间，延时时间短的放在前面
     * ScheduledFutureTask会保存其在DelayedWorkQueue中的索引，work在取任务时总是获取数组的第一个，延时执行就是通过awaitNanos(delayTime)
     * 起的作用，ScheduledExecutorService.scheduleAtFixedRate,ScheduledExecutorService.scheduleWithFixedDelay都是在业务方法执行完成后，会
     * 重置FutureTask的状态，然后重新将该ScheduledFutureTask添加到队列中，并重新计算延时时间，scheduleAtFixedRate就是initialDelay + delay
     * scheduleWithFixedDelay是，now() + delay
     *
     */
    public static void test6() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.schedule(()->{
                System.out.println("ssss");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    Thread.interrupted();
                }
            }, 10 - i, TimeUnit.HOURS);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        executorService.shutdownNow();
//        executorService.execute(()-> {
//            System.out.println("sdfs");
//        });
    }

}
