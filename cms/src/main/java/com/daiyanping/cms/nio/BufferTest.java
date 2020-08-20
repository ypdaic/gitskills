package com.daiyanping.cms.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferTest {

    public static void main(String[] args) {
//        allocateTest();
operateTest();


    }

    /**
     * 堆内存分配与直接内存分配耗时比较
     *
     * 堆内存分配耗时：18ms
     * 直接内存分配耗时：381ms
     */
    private static void allocateTest() {
        long l = System.currentTimeMillis();
        int count = 1000000;

        for (int i = 0; i < count; i++) {
            ByteBuffer allocate = ByteBuffer.allocate(2);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("堆内存分配耗时：" + (endTime - l) + "ms");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            ByteBuffer allocate = ByteBuffer.allocateDirect(2);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("直接内存分配耗时：" + (endTime2 - startTime) + "ms");
    }

    /**
     * 堆内存操作与直接内存读写耗时对比
     *
     * 堆内存分配及操作耗时：93ms
     * 直接内存分配及操作耗时：52ms
     */
    private static void operateTest() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100000000 * 2);

        int count = 100000000;
        long l = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            byteBuffer.putChar('a');
        }
        byteBuffer.flip();
        for (int i = 0; i < count; i++) {
            byteBuffer.getChar();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("堆内存分配及操作耗时：" + (endTime - l) + "ms");

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(100000000 * 2);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            byteBuffer2.putChar('a');
        }
        byteBuffer2.flip();
        for (int i = 0; i < count; i++) {
            byteBuffer2.getChar();
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("直接内存分配及操作耗时：" + (endTime2 - startTime) + "ms");
    }
}
