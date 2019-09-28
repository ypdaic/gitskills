package com.daiyanping.cms.javabase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ThreadTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-05
 * @Version 0.1
 */
public class ThreadTest {

    private static Object object = new Object();
    private static Object object2 = new Object();


    public static void main(String[] args) {

//        test1();

//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
//        test7();
        test8();
    }

    /**
     * 在wait中的线程被中断唤醒后，需要再次获取锁，锁获取成功后抛出中断异常，并清空中断标志
     */
    public static void test1() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread1被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread2线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        thread.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        thread2.interrupt();
    }

    /**
     * 在锁重入的情况下，wait方法一样会完整的释放锁
     */
    public static void test2() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                synchronized (object) {
                    synchronized (object) {

                        try {
                            object.wait();
                            System.out.println("thread1被唤醒");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println("thread线程的中断标志" + Thread.currentThread().isInterrupted());
                        }
                    }
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread2线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        thread.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
        thread.interrupt();
    }

    /**
     * wait 方法只会使线程释放目标对象的同步锁，而不会使线程释放其他同步锁
     */
    public static void test3() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                synchronized (object) {
                    synchronized (object2) {

                        try {
                            object2.wait();
                            System.out.println("thread1被唤醒");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println("thread线程的中断标志" + Thread.currentThread().isInterrupted());
                        }
                    }
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread2线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        thread.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
//        thread.interrupt();
    }

    /**
     * notify 方法只会唤醒目标对象等待列表中的一个线程，具体是哪个线程无法得知
     * notify 方法不会出现锁竞争
     */
    public static void test4() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread1被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread2线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        thread.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (object) {

            object.notify();
        }
    }

    /**
     * notifyAll 方法会唤醒目标对象等待列表中的所有线程，具体哪个线程先获得锁无从得知
     *
     */
    public static void test5() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread1被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait();
                    System.out.println("thread2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread2线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        thread.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (object) {

            object.notifyAll();
        }

    }

    /**
     * wait(0),wati(0,0) 相当于 wait()
     *
     */
    public static void test6() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait(0);
                    System.out.println("thread1被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (object) {
                try {
                    object.wait(0, 0);
                    System.out.println("thread2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread2线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        thread.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * sleep(0); 可以重新触发一次cpu竞争
     *
     */
    public static void test7() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread thread = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread1开始执行");
            synchronized (object) {
                try {
                    object.wait(0);
                    System.out.println("thread1被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread2开始执行");
            synchronized (object) {
                try {
                    object.wait(0, 0);
                    System.out.println("thread2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("thread2线程的中断标志" + Thread.currentThread().isInterrupted());
                }
            }

        });

        thread.start();
        thread2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    /**
     * await使用的LockSupport.park(this);阻塞的当前线程，如果在阻塞期间调用
     * 当前线程的interrupt() 方法，当前线程会被唤醒，和wait是一样的
     */
    public static void test8() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        Thread thread = new Thread(() -> {
            reentrantLock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }

        });

        thread.start();

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();


    }
}
