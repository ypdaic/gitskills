package com.daiyanping.cms.jvm;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OOM {

    /**
     * 测试MinGC,FullGC
     *
     * [GC (Allocation Failure) [PSYoungGen: 2048K->504K(2560K)] 2048K->979K(9728K), 2.5625264 secs] [Times: user=0.00 sys=0.00, real=2.56 secs]
     * [GC (Allocation Failure) [PSYoungGen: 2552K->504K(2560K)] 3027K->1550K(9728K), 0.0070964 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
     * 0
     * [GC (Allocation Failure) [PSYoungGen: 2543K->504K(2560K)] 3590K->2547K(9728K), 0.0112763 secs] [Times: user=0.05 sys=0.00, real=0.01 secs]
     * 100
     * 200
     * [GC (Allocation Failure) [PSYoungGen: 2551K->504K(4608K)] 4594K->4574K(11776K), 0.0021865 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * 300
     * 400
     * 500
     * 600
     * [GC (Allocation Failure) [PSYoungGen: 4594K->504K(4608K)] 8664K->8637K(12800K), 0.0050620 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
     * [Full GC (Ergonomics) [PSYoungGen: 504K->497K(4608K)] [ParOldGen: 8133K->7963K(14848K)] 8637K->8460K(19456K), [Metaspace: 3518K->3518K(1056768K)], 0.0269788 secs] [Times: user=0.05 sys=0.00, real=0.03 secs]
     * 700
     * 800
     * 900
     * Heap
     *  PSYoungGen      total 4608K, used 3844K [0x00000000e0780000, 0x00000000e1880000, 0x0000000100000000)
     *   eden space 4096K, 81% used [0x00000000e0780000,0x00000000e0ac4be8,0x00000000e0b80000)
     *   from space 512K, 97% used [0x00000000e0b80000,0x00000000e0bfc450,0x00000000e0c00000)
     *   to   space 4608K, 0% used [0x00000000e1400000,0x00000000e1400000,0x00000000e1880000)
     *  ParOldGen       total 14848K, used 7963K [0x00000000a1600000, 0x00000000a2480000, 0x00000000e0780000)
     *   object space 14848K, 53% used [0x00000000a1600000,0x00000000a1dc6cd0,0x00000000a2480000)
     *  Metaspace       used 3547K, capacity 4496K, committed 4864K, reserved 1056768K
     *   class space    used 381K, capacity 388K, committed 512K, reserved 1048576K
     * @param args
     */
    public static void main(String[] args) {
//        GCRoot包含，方法区中类静态属性引用的对象
//        方法区中常量引用的对象。
//        虚拟机栈(本地变量表)中引用的对象
//        本地方法栈JNI(Native方法)中引用的对象
//
//        这里bytes 在方法执行过程中就是GCRoot
        ArrayList<byte[]> bytes = new ArrayList<>();
        try {
            for (int i = 0; i < 10000; i++) {
                bytes.add(new byte[1024 * 100]);
                if (i % 100 == 0) {
                    System.out.println(i);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
