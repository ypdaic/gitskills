package com.daiyanping.cms.concurrent.test;

import com.daiyanping.cms.concurrent.A;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "enjoy")
public class TestJol {

    static A l = new A();

    static Thread t1;
    static  Thread t2;
    static List<A> list = new ArrayList<A>();

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException, IllegalAccessException {


//      // System.out.println(Integer.toHexString(l.hashCode()));
//        log.debug("线程还未启动----无锁");
//        //log.debug(ClassLayout.parseInstance(l).toPrintable());
//
//
        t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    Thread.sleep(200);

                    log.debug(ClassLayout.parseInstance(l).toPrintable());
                }
            }
        };

        t1.start();

        for (int i = 0; i < 999999; i++) {
            //Thread.sleep(2);
            list.add(new A());
        }
//
//        t2 = new Thread(){
//            @Override
//            public void run() {
//                testLock();
//            }
//        };
//        t1.setName("t1");
//
//        t1.start();
//        //等待t1执行完后再启动t2
//        t1.join();
//
//        t2.setName("t2");
//        t2.start();


    }

    /**
     * synchronized 如果是同一个线程加锁
     * 交替执行 轻量锁
     * 资源竞争----mutex
     *
     *
     */

    public static void testLock(){
        //偏向锁  首选判断是否可偏向  判断是否偏向了 拿到当前的id 通过cas 设置到对象头
        synchronized (l){//t1 locked  t2 ctlock
            log.debug("name:"+Thread.currentThread().getName());
            //有锁  是一把偏向锁
            log.debug(ClassLayout.parseInstance(l).toPrintable());
        }

    }
}
