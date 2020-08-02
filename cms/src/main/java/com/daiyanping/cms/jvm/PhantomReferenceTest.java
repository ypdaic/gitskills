package com.daiyanping.cms.jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {

    public static void main(String[] args) {
        Object obj = new Object();
        ReferenceQueue referenceQueue = new ReferenceQueue();
        PhantomReference<Object> pf = new PhantomReference<Object>(obj, referenceQueue);
        obj=null;
        System.out.println(pf.get());//永远返回null
        // pf.isEnQueued();//返回是否从内存中已经删除
        System.out.println(referenceQueue.poll());

        System.gc();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 回收前被放到引用队列
        System.out.println(referenceQueue.poll());
    }
}
