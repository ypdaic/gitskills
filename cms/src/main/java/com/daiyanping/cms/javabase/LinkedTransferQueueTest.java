package com.daiyanping.cms.javabase;

import java.util.concurrent.LinkedTransferQueue;

/**
 * @ClassName LinkedTransferQueueTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-04
 * @Version 0.1
 */
public class LinkedTransferQueueTest {

    /**
     * 非阻塞的queue 源码：https://www.jianshu.com/p/ae6977886cec
     *
     * 注意：队列中永远只有一种类型的操作,要么是 put 类型, 要么是 take 类型.
     * put 线程，1：先看看head 是不是take类型，如果是直接交出数据
     *          2： 如果不是，追加队列，立刻返回
     *
     * take 线程：1: head 是不是put 操作，如果是直接拿走数据
     *            2: 如果不是，追加到tail 并阻塞
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        LinkedTransferQueue<String> strings = new LinkedTransferQueue<>();

        strings.offer("sss");
        strings.offer("sssd");
        try {
            strings.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * private E xfer(E e, boolean haveData, int how, long nanos) {
     *     if (haveData && (e == null))
     *         throw new NullPointerException();
     *     Node s = null;                        // the node to append, if needed
     *
     *     retry:
     *     for (;;) {                            // restart on append race
     *         // 从  head 开始
     *         for (Node h = head, p = h; p != null;) { // find & match first node
     *             // head 的类型。
     *             boolean isData = p.isData;
     *             // head 的数据
     *             Object item = p.item;
     *             // item != null 有 2 种情况,一是 put 操作, 二是 take 的 itme 被修改了(匹配成功)
     *             // (itme != null) == isData 要么表示 p 是一个 put 操作, 要么表示 p 是一个还没匹配成功的 take 操作
     *             if (item != p && (item != null) == isData) {
     *                 // 如果当前操作和 head 操作相同，就没有匹配上，结束循环，进入下面的 if 块。
     *                 if (isData == haveData)   // can't match
     *                     break;
     *                 // 如果操作不同,匹配成功, 尝试替换 item 成功,
     *                 if (p.casItem(item, e)) { // match
     *                     // 更新 head
     *                     for (Node q = p; q != h;) {
     *                         Node n = q.next;  // update by 2 unless singleton
     *                         if (head == h && casHead(h, n == null ? q : n)) {
     *                             h.forgetNext();
     *                             break;
     *                         }                 // advance and retry
     *                         if ((h = head)   == null ||
     *                             (q = h.next) == null || !q.isMatched())
     *                             break;        // unless slack < 2
     *                     }
     *                     // 唤醒原 head 线程.
     *                     LockSupport.unpark(p.waiter);
     *                     return LinkedTransferQueue.<E>cast(item);
     *                 }
     *             }
     *             // 找下一个
     *             Node n = p.next;
     *             p = (p != n) ? n : (h = head); // Use head if p offlist
     *         }
     *         // 如果这个操作不是立刻就返回的类型
     *         if (how != NOW) {                 // No matches available
     *             // 且是第一次进入这里
     *             if (s == null)
     *                 // 创建一个 node
     *                 s = new Node(e, haveData);
     *             // 尝试将 node 追加对队列尾部，并返回他的上一个节点。
     *             Node pred = tryAppend(s, haveData);
     *             // 如果返回的是 null, 表示不能追加到 tail 节点,因为 tail 节点的模式和当前模式相反.
     *             if (pred == null)
     *                 // 重来
     *                 continue retry;           // lost race vs opposite mode
     *             // 如果不是异步操作(即立刻返回结果)
     *             if (how != ASYNC)
     *                 // 阻塞等待匹配值
     *                 return awaitMatch(s, pred, e, (how == TIMED), nanos);
     *         }
     *         return e; // not waiting
     *     }
     * }
     */
}
