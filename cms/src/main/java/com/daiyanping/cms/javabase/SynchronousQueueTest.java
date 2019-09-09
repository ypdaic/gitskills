package com.daiyanping.cms.javabase;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SynchronousQueueTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-04
 * @Version 0.1
 */
public class SynchronousQueueTest {


    public static void main(String[] args) {
        test2();
    }

    /**
     * SynchronousQueue默认使用先进后出的TransferStack，TransferStack内部使用SNode保存数据，
     * SNode 包含这些属性
     * volatile SNode next;        // next node in stack 下一个节点
     *
     * volatile SNode match;       // the node matched to this 节点是否匹配了
     * volatile Thread waiter;     // to control park/unpark   需要阻塞的线程
     * Object item;                // data; or null for REQUESTs  节点保存的数据
     * int mode;   节点的类型，是请求类型，还是数据类型，还是数据填充类型
     *
     * 当多个线程在操作SynchronousQueue，且是请求类型时，SNode会形成一个单向链表，最后进的，最先出来，
     * 当有一个offer操作时，会首先生成一个FULFILLING节点，并置为head，然后获取next，将next的match指向自己，并
     * 释放在next上等待的线程。然后设置next的next为head节点
     */
    public static void test1() {
        SynchronousQueue<String> strings = new SynchronousQueue<>();
        try {
            strings.poll(0, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                strings.take();
            } catch (InterruptedException e) {
            }
        }).start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            try {
                strings.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            strings.offer("sfsf");
        }).start();
    }

    /**
     * 当给true时，会使用队列来保存，默认会创建一个空的head,tail QNode 节点
     *
     *
     *      * QNode 包含这些属性
     *      volatile QNode next;          // next node in queue 下个节点
     *      volatile Object item;         // CAS'ed to or from null 节点保存的数据
     *      volatile Thread waiter;       // to control park/unpark 节点等待的线程
     *      final boolean isData;        是否为数据节点
     *      *
     *      * 当多个线程在操作SynchronousQueue，且是请求类型时，QNode会形成一个单向链表，是先进先出的
     *      * 当有一个offer操作时，会获取首节点，并设置首节点next节点的item属性，然后释放在next上等待的线程，然后设置next的next为首节点
     *      *
     */
    public static void test2() {
        SynchronousQueue<String> strings = new SynchronousQueue<>(true);
        try {
            strings.poll(0, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                strings.take();
            } catch (InterruptedException e) {
            }
        }).start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            try {
                strings.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
//
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            strings.offer("sfsf");
        }).start();
    }
}
