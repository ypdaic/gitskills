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
}
