package com.daiyanping.cms.javabase;

/**
 * @ClassName SynAndWaitTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-24
 * @Version 0.1
 */
public class SynAndWaitTest {

    /**
     * 测试wait,notify 方法对同一个对象生效，不同的对象之间不影响
     * @param args
     */
    public static void main(String[] args) {
        Object o = new Object();
        Object o1 = new Object();

        new Thread(() -> {
            synchronized (o) {
                System.out.println("线程1执行");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("线程1执行结束");
            }
        }).start();

        new Thread(() -> {
            synchronized (o) {
                System.out.println("线程2执行");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("线程2执行结束");
            }
        }).start();

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (o) {
                System.out.println("线程3执行");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                o.notify();
                System.out.println("线程3执行结束");
            }
        }).start();

        new Thread(() -> {
            synchronized (o1) {
                System.out.println("线程4执行");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    o1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("线程4执行结束");
            }
        }).start();

        new Thread(() -> {
            synchronized (o1) {
                System.out.println("线程5执行");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    o1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("线程5执行结束");
            }
        }).start();

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (o1) {
                System.out.println("线程6执行");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                o1.notify();
                System.out.println("线程6执行结束");
            }
        }).start();


    }
}
