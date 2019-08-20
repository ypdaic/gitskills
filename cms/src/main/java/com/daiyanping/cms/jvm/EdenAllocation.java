package com.daiyanping.cms.jvm;

public class EdenAllocation {

    /**
     * 通过参数设定jvm堆内存大小 -Xms20m 表示堆最小20m,-Xmx20m 表示堆最大20m,-Xmn10m 表示新生代10m
     * -XX:+PrintGCDetails 表示打印GC信息
     * -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails
     *
     * 新生代又分为eden,from,to 默认比例为8:1:1 表示eden占80%，from占10%，to占10%，可以通过参数
     * -XX:SurvivorRatio=8 进行调节等于8表示8:1:1 如果为2就是2:1:1
     * 从下面的打印结果可以看到就是eden为8m,from为1m,to为1m
     * from,to为1:1是因为使用了复制回收算法，必须是1:1
     *
     *Heap
     *  PSYoungGen      total 9216K, used 5691K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     *   eden space 8192K, 69% used [0x00000000ff600000,0x00000000ffb8ed30,0x00000000ffe00000)
     *   from space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
     *   to   space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
     *  ParOldGen       total 10240K, used 0K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     *   object space 10240K, 0% used [0x00000000fec00000,0x00000000fec00000,0x00000000ff600000)
     *  Metaspace       used 3519K, capacity 4496K, committed 4864K, reserved 1056768K
     *   class space    used 378K, capacity 388K, committed 512K, reserved 1048576K
     *
     * 我们添加上-XX:SurvivorRatio=4 也就是eden:from:to为4:1:1打印结果如下
     * eden是占据了7m
     * Heap
     *  PSYoungGen      total 8704K, used 5554K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     *   eden space 7168K, 77% used [0x00000000ff600000,0x00000000ffb6ca88,0x00000000ffd00000)
     *   from space 1536K, 0% used [0x00000000ffe80000,0x00000000ffe80000,0x0000000100000000)
     *   to   space 1536K, 0% used [0x00000000ffd00000,0x00000000ffd00000,0x00000000ffe80000)
     *  ParOldGen       total 10240K, used 0K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     *   object space 10240K, 0% used [0x00000000fec00000,0x00000000fec00000,0x00000000ff600000)
     *  Metaspace       used 3431K, capacity 4496K, committed 4864K, reserved 1056768K
     *   class space    used 370K, capacity 388K, committed 512K, reserved 1048576K
     *
     *   上面两个结果都是在a,b变量没有分配内存的情况
     *
     *   下面对a,b变量进行分配内存
     *   可以看到我们的对象都是优先在eden进行分配
     *
     *   Heap
     *  PSYoungGen      total 9216K, used 7739K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     *   eden space 8192K, 94% used [0x00000000ff600000,0x00000000ffd8ed50,0x00000000ffe00000)
     *   from space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
     *   to   space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
     *  ParOldGen       total 10240K, used 0K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     *   object space 10240K, 0% used [0x00000000fec00000,0x00000000fec00000,0x00000000ff600000)
     *  Metaspace       used 3435K, capacity 4496K, committed 4864K, reserved 1056768K
     *   class space    used 371K, capacity 388K, committed 512K, reserved 1048576K
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        byte[] a,b;
        a = new byte[1024*1024];
        b = new byte[1024*1024];
    }
}
