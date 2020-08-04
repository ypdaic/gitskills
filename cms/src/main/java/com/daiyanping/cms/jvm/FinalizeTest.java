package com.daiyanping.cms.jvm;

import java.util.Objects;

/**
 * @ClassName FinalizeTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/7/23
 * @Version 0.1
 */
public class FinalizeTest {

    public static FinalizeTest f;

    /**'
     * 此方法只能调一次
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize 方法被执行");
        f = this;
    }

    /**
     * 验证finalize 方法只能被执行一次
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        f = new FinalizeTest();
        f = null;
        System.gc();
        // 因为Finalizer线程优先级很低，暂停2秒，已等待它
        Thread.sleep(1000);
        if (Objects.nonNull(f)) {
            System.out.println("对象还存活");
        } else {
            System.out.println("对象被回收");
        }
        f = null;
        System.gc();
        Thread.sleep(1000);
        if (Objects.nonNull(f)) {
            System.out.println("对象还存活");
        } else {
            System.out.println("对象被回收");
        }
//
//        f = null;
//        System.gc();
////        Thread.sleep(1000);
//        if (Objects.nonNull(f)) {
//            System.out.println("对象还存活");
//        } else {
//            System.out.println("对象被回收");
//        }


    }
}
