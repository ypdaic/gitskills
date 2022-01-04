package com.daiyanping.cms.concurrent;

import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;

public class TestJol {
    static L l = new L();


    public static void main(String[] args) {
//        test12();
        test1();
    }

    /**
     * 前8个字节表示markword, 后4个字节表示class point(就是指向类对应class对象的地址)
     * com.daiyanping.cms.concurrent.L object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE        101 无锁可偏向
     *       0     4        (object header)                           01 00 00 00 (00000101 00000000 00000000 00000000) (5)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    private static void test1() {
        String s = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s);

//        new Thread(() -> {
//            ArrayList<Object> objects = new ArrayList<>();
//            for (int i = 0; i < 9999; i++) {
//                try {
//                    Thread.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                objects.add(new A());
//            }
//        }).start();
//
//        /**
//         * 验证对象分代年龄的变化
//         * -XX:BiasedLockingStartupDelay=0 -Xms200m -Xmx200m
//         */
//        while (true) {
//            try {
//                Thread.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            String g = ClassLayout.parseInstance(l).toPrintable();
//            System.out.println(g);
//        }
    }

    /**
     * 0：偏向标识（0：不可偏向，1：可偏向）
     * 01：表示偏向锁
     * cac736f
     * com.daiyanping.cms.concurrent.L object internals: 存储方式是小端存储也就是左边的低位右边的是高位
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE        第一位不使用     001表示无锁不可偏向（因为计算了hashcode）                24位 表示hashcode
     *       0     4        (object header)                           01 6f 73 ac (0           0000001                         01101111 01110011 10101100) (-1401721087)
     *                                                                             1位         7位hoshcode                     24位不使用
     *       4     4        (object header)                           0c 00 00 00 (0           0001100                        00000000 00000000 00000000) (12)
     *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    private static void test2() {
        int i = l.hashCode();
        System.out.println(Integer.toHexString(i));
        String s = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s);
    }

    /**-XX:BiasedLockingStartupDelay=0 关闭偏向延迟
     * com.daiyanping.cms.concurrent.L object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE       101 表示无锁可偏向
     *       0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     *
     * com.daiyanping.cms.concurrent.L object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0     4        (object header)                           05 b8 ac 02 (00000101 10111000 10101100 00000010) (44873733)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    private static void test3() {
        // 无锁可偏向，但是没有偏向
        String s = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s);
        synchronized (l) {
            // 有锁  是一把偏向锁
            String s2 = ClassLayout.parseInstance(l).toPrintable();
            System.out.println(s2);
        }
    }

    private static void test90() {
        // 无锁可偏向，但是没有偏向
        String s = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s);
        synchronized (l) {
            // 有锁  是一把偏向锁
            String s2 = ClassLayout.parseInstance(l).toPrintable();
            System.out.println(s2);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 有锁  是一把偏向锁
            s2 = ClassLayout.parseInstance(l).toPrintable();
            System.out.println(s2);
        }

        synchronized (l) {
            // 有锁  是一把偏向锁
            String s2 = ClassLayout.parseInstance(l).toPrintable();
            System.out.println(s2);
        }
    }

    /**
     * cac736f
     * com.daiyanping.cms.concurrent.L object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE        001无锁不可偏向
     *       0     4        (object header)                           01 6f 73 ac (00000001 01101111 01110011 10101100) (-1401721087)
     *       4     4        (object header)                           0c 00 00 00 (00001100 00000000 00000000 00000000) (12)
     *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     *
     * com.daiyanping.cms.concurrent.L object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE         000轻量锁 线程id
     *       0     4        (object header)                           58 f4 3a 03 (01011000 11110100 00111010 00000011) (54195288)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    private static void test4() {
        // 无锁可偏向，但是没有偏向
        int i = l.hashCode();
        System.out.println(Integer.toHexString(i));
        // 001 无锁不可偏向
        String s = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s);
        synchronized (l) {
            // 000 轻量级锁
            String s2 = ClassLayout.parseInstance(l).toPrintable();
            System.out.println(s2);
        }
    }

    private static void test5() {
        Thread thread = new Thread(() -> {
            synchronized (l) {

            }
        });

        Thread thread2 = new Thread(() -> {
            /**
             * com.daiyanping.cms.concurrent.L object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           c0 f3 6a 21 (11000000 11110011 01101010 00100001) (560657344)
             *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
             *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
             *      12     4        (loss due to the next object alignment)
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
             */
            synchronized (l) {
                // 000 轻量级锁，这里没法发生竞争
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void test6() {
        Thread thread = new Thread(() -> {
            /**
             * com.daiyanping.cms.concurrent.L object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           ca af a1 1c (11001010 10101111 10100001 00011100) (480358346)
             *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
             *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
             *      12     4        (loss due to the next object alignment)
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
             */
            synchronized (l) {
                // 010 重量锁，这里发生了竞争
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });

        Thread thread2 = new Thread(() -> {
            /**
             * com.daiyanping.cms.concurrent.L object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           ca af a1 1c (11001010 10101111 10100001 00011100) (480358346)
             *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
             *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
             *      12     4        (loss due to the next object alignment)
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
             */
            synchronized (l) {
                // 010 重量锁，这里发生了竞争
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });
        thread.start();
        thread2.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void test7() {
        // 无锁可偏向，但是没有偏向
        int i = l.hashCode();
        System.out.println(Integer.toHexString(i));
        /**
         * com.daiyanping.cms.concurrent.L object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           01 6f 73 ac (00000001 01101111 01110011 10101100) (-1401721087)
         *       4     4        (object header)                           0c 00 00 00 (00001100 00000000 00000000 00000000) (12)
         *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
         *      12     4        (loss due to the next object alignment)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         */
        // 001 无锁不可偏向
        String s = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s);
        Thread thread = new Thread(() -> {
            /**
             * com.daiyanping.cms.concurrent.L object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           00 f6 ce 21 (00000000 11110110 11001110 00100001) (567211520)
             *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
             *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
             *      12     4        (loss due to the next object alignment)
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
             */
            synchronized (l) {
                // 000 轻量锁
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });

        Thread thread2 = new Thread(() -> {
            /**
             * com.daiyanping.cms.concurrent.L object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           a0 f3 de 21 (10100000 11110011 11011110 00100001) (568259488)
             *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
             *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
             *      12     4        (loss due to the next object alignment)
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
             */
            synchronized (l) {
                // 000 轻量锁
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void test8() {

        /**
         * com.daiyanping.cms.concurrent.L object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
         *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
         *      12     4        (loss due to the next object alignment)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         * 101 无锁可偏向
         */
        String s = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s);
        Thread thread = new Thread(() -> {
            /**
             * com.daiyanping.cms.concurrent.L object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           05 e0 9b 20 (00000101 11100000 10011011 00100000) (547086341)
             *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
             *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
             *      12     4        (loss due to the next object alignment)
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
             */
            synchronized (l) {
                // 101 有锁可偏向
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });

        Thread thread2 = new Thread(() -> {
            /**
             * com.daiyanping.cms.concurrent.L object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           05 e0 9b 20 (00000101 11100000 10011011 00100000) (547086341)
             *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
             *       8     4        (object header)                           43 c1 00 f8 (01000011 11000001 00000000 11111000) (-134168253)
             *      12     4        (loss due to the next object alignment)
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
             */
            synchronized (l) {
                // 101 有锁可偏向,获取线程id，通过cas 设置对象头
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 某个线程释放偏向锁后，锁对象的锁标识还是偏向锁，并且任然指向之前加锁的线程，即使该线程已经退出了
     */
    private static void test10() {
        Thread thread = new Thread(() -> {
            synchronized (l) {
                // 101 有锁可偏向
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s2 = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s2);


    }

    /**
     * 某个线程释放偏向锁后，锁对象的锁标识还是偏向锁，并且任然指向之前加锁的线程，即使该线程已经退出了
     */
    private static void test12() {
        Thread thread = new Thread(() -> {
            synchronized (l) {
                System.out.println(Thread.currentThread().getId());
                // 101 有锁可偏向
                String s2 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s2);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s2 = ClassLayout.parseInstance(l).toPrintable();
        System.out.println(s2);

         new Thread(() -> {
            synchronized (l) {
                System.out.println(Thread.currentThread().getId());
                // 101 有锁可偏向
                String s3 = ClassLayout.parseInstance(l).toPrintable();
                System.out.println(s3);
            }
        }).start();
    }

    /**
     * 模拟偏向延时之后获取锁才是偏向锁
     */
    private static void test11() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        A a = new A();
        synchronized (a) {
            // 101 有锁可偏向
            String s2 = ClassLayout.parseInstance(a).toPrintable();
            System.out.println(s2);
        }



        synchronized (a) {
            // 101 有锁可偏向
            String s2 = ClassLayout.parseInstance(a).toPrintable();
            System.out.println(s2);
        }

    }
}
