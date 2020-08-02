package com.daiyanping.cms.jvm.OOM;

import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;

import java.nio.ByteBuffer;

/**
 *
 *  jvm 参数配置
 *  -Xms20m -Xmx20m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
 * NIO程序使用ByteBuffer.allocteDirect(capability) 方式分配OS 本地内存，不属于GC管辖范围，由于不需要内存拷贝所以速度相对较快
 * 但是如果不断分配本地内存，堆内存很少使用，那么JVM就不需要执行GC，DirectByteBuffer 对象们就不会被回收，
 * 这时候堆内存充足，但是本地内存可能已经使用光了，再次尝试分配本地内存时就会出现OutOfMemoryError
 */
public class DirectBufferMemoryError {

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory
     * @param args
     */
    public static void main(String[] args) {

        // 最大的直接内存 4g
        System.out.println("配置的maxDirectMemory:" + (sun.misc.VM.maxDirectMemory() / 1024 / 1024) + "MB");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(1024 * 1024 * 5);
    }

}
