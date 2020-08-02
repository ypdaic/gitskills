package com.daiyanping.cms.jvm.OOM;

import java.util.ArrayList;

/**
 * jvm 参数配置
 * -Xms20m -Xmx20m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
 *
 * gc 回收时间过长时会抛出该异常，过长的定义是98%的时间用来做gc并且回收了不到2%的堆内存
 * 连续多次回收都只回收不到2%的极端情况下才会抛出，假如不抛出GC overhead limit exceeded
 * 会发生什么情况，那就是gc清理的这么点内存很快会再次被填满，迫使GC 再次执行，这样就形成恶性
 * 循环CPU使用率一直是100%，而gc却没有任何成果
 */
public class GCOverHeadTest {

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
     *
     * 频繁进行Full GC 但是回收了一点点的空间
     * @param args
     */
    public static void main(String[] args) {
        int i = 0;

        ArrayList<String> list = new ArrayList<>();
        try {
            while (true) {
                list.add(String.valueOf(++i).intern());
            }
        } catch (Throwable e) {
            System.out.println("**********i: " + i);
            e.printStackTrace();
            throw e;
        }
    }
}
