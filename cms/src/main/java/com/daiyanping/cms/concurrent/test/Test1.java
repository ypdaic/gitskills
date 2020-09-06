package com.daiyanping.cms.concurrent.test;

import com.daiyanping.cms.concurrent.L;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "enjoy")
public class Test1 {


    Thread t1;
    Thread t2;
    CASLock c =new CASLock();
    int k =0;



    L l = new L();

    public static void main(String[] args) throws InterruptedException {
        Test1 test1 = new Test1();
        test1.start();
    }

    public void start() throws InterruptedException {
            t1 = new Thread(){
                @Override
                public void run() {
                    sum();
                }
            };

         t2 = new Thread(){
            @Override
            public void run() {
                sum();
            }
        };

         t1.start();
         t2.start();
        //阻塞
         t1.join();
         t2.join();
         //2w
        System.out.println(k);
    }

    public void sum(){
        /**
         * 锁 对象当中的一个标识
         * 加锁：就是去改变这个对象的标识的值
         * 加锁成功：让方法正常返回
         * 加锁失败：让失败的这个线程 死循环 阻塞
         */
       // c.lock();
            //业内一般叫做锁对象
        //synchronized究竟改变了l对象的什么东西
            synchronized (l) {

                for (int i = 0; i <= 9999; i++) {
                    k = k + 1;
                }
            }

       // c.unlock();

    }
    //12月份之前 都不能有多个线程访问 sun





}
