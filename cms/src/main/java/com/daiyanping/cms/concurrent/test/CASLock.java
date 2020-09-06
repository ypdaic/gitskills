package com.daiyanping.cms.concurrent.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

public class CASLock {
    private volatile int status=0;//标识---是否有线程在同步块-----是否有线程上锁成功
    //获取Unsafe对象，只能这么获取，Unsafe这个类比较难有兴趣同学可以自己研究研究
    //窃以为Unsafe是java里面最牛逼的一个对象，没有之一
    private static final Unsafe unsafe = getUnsafe();

    //定义一个变量来记录 volatile int status的地址
    //因为CAS需要的是一个地址,于是就定义这个变量来标识status在内存中的地址
    private static long valueOffset = 0;

    /**
     * 初始化的获取status在内置的偏移量
     * 说白了就是status在内存中的地址
     * 方便后面对他进行CAS操作
     */
    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (CASLock.class.getDeclaredField("status"));
        } catch (Exception ex) { throw new Error(ex); }
    }


    /**
     * 加锁方法
     */
    public void lock(){
        /**
         * 判断status是否=0；如果等于0则改变成为1
         * 而判断赋值这两个操作可以通过一个叫做CAS的技术来完成
         * 通过cas去改变status的值，如果是0就改成1
         * 思考一下为什么要用CAS
         * 关于CAS如果你不了解可以先放放，后面我们讲
         * 目前就认为CAS用来赋值的和 = 的效果一样
         */
        while(!compareAndSet(0,1)){
            //加锁失败会进入到这里空转
        }

       // 如果加锁成功则直接正常返回
    }

    //为什么unlock不需要CAS呢？可以自己考虑一下，如果不懂可以讨论
    public void unlock(){
        status=0;
    }

    boolean compareAndSet(int except,int newValue){
        //如果 valueOffset或者 status这个变量 = except 那么改成 newValue
      return unsafe.compareAndSwapInt(this,valueOffset,except,newValue);
    }


    /**
     * 获取Unsafe对象
     * @return
     */
    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe)field.get(null);

        } catch (Exception e) {
        }
        return null;
    }

}
