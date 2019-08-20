package com.daiyanping.cms.jvm;

public class BigAllocation {

    /**
     * jvm参数：-Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails
     * 当新生代放不下的大对象，会直接放入老年代，默认情况
     *
     * 查看打印结果，a,b是放在了eden区，而c直接放在了老年代
     * Heap
     *  PSYoungGen      total 9216K, used 7739K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     *   eden space 8192K, 94% used [0x00000000ff600000,0x00000000ffd8ed30,0x00000000ffe00000)
     *   from space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
     *   to   space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
     *  ParOldGen       total 10240K, used 5120K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     *   object space 10240K, 50% used [0x00000000fec00000,0x00000000ff100010,0x00000000ff600000)
     *  Metaspace       used 3414K, capacity 4496K, committed 4864K, reserved 1056768K
     *   class space    used 366K, capacity 388K, committed 512K, reserved 1048576K
     *
     *  判断一个对象是否直接放入老年代，也是有参数设置的
     *  -XX:PretenureSizeThreshold=2m 这个参数表示当我们的对象大于2m时，直接放入到老年代，
     *  上面的参数只是对只对Serial和ParNew两款收集器有效。所以我们还需要指定垃圾回收器
     *  -XX:+UseSerialGC
     *
     * @param args
     */
    public static void main(String[] args) {
        byte[] a,b,c;
        a = new byte[1024*1024];
        b = new byte[1024*1024];
        c = new byte[1024*1024 * 3];
    }
}
