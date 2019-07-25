package com.daiyanping.cms.AQS;

import lombok.Data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @ClassName AtomicMarkableReferenceTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-25
 * @Version 0.1
 */
public class AtomicMarkableReferenceTest {

    private static Test test = new Test();
    private static AtomicMarkableReference atomicMarkableReference = new AtomicMarkableReference<Test>(test, false);
    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] agrs) {
        new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Test test2 = new Test();
            System.out.println("线程1:" + System.identityHashCode(test2));
            Object reference = atomicMarkableReference.getReference();
            boolean b = atomicMarkableReference.compareAndSet(reference, test2, false, true);
            System.out.println("线程1是否执行成功" + b);
//            boolean marked = atomicMarkableReference.isMarked();
//            System.out.println("检查是否出现ABA问题1:" + marked);
            System.out.println("线程1:" + System.identityHashCode(atomicMarkableReference.getReference()));
        }).start();

        new Thread(() -> {
            countDownLatch.countDown();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Test test2 = new Test();
            System.out.println("线程2:" + System.identityHashCode(test2));
            Object reference = atomicMarkableReference.getReference();
            boolean b = atomicMarkableReference.compareAndSet(reference, test2, false, true);
            System.out.println("线程2是否执行成功:" + b);
//            boolean marked = atomicMarkableReference.isMarked();
//            System.out.println("检查是否出现ABA问题2:" + marked);
            System.out.println("线程2:" + System.identityHashCode(atomicMarkableReference.getReference()));
        }).start();

    }

    @Data
    private static class Test {
        private String name;
    }
}
