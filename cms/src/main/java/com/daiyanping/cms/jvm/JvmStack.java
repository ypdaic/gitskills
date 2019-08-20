package com.daiyanping.cms.jvm;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * 使用Javap -p -v ...class > test.txt  可以反编译字节码
 * 线程共享：方法区，堆，方法区保存有类信息，常量，静态变量，即时编译期编译后的代码
 * 线程私有：程序计数器，虚拟机栈，本地方法栈
 *
 * jdk1.7永久代和1.8元空间的区别，永久代使用的回收策略和堆的回收策略差不多
 * 1.8考虑到这种方式是不适合的，就弄了个元空间和堆却别开来
 */
public class JvmStack {

    final String Fs =  "常量放在方法区";

    static String Ss = "静态属性放在方法区";

    // 实例变量放在堆中
    int count = 0;

    /**
     * 线程共享区域，堆，方法区
     * @param a
     * @return
     */
    public int test(int a) {
        count++;
        System.out.println(Ss);
        int b = a -100;
        int i = test2(b);
        System.out.println(Fs);
        return i;
    }

    public int test2(int c) {
        int d = c -100;
        return d;
    }

    public static void main(String[] args) {
        JvmStack jvmStack = new JvmStack();
        jvmStack.test(1000);

        // 虽然直接内存不受jvm管理，但是如果直接内存大于jvm内存设置，一样会抛OOM
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128 * 1024 * 1024);
    }
}
