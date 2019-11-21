package com.daiyanping.cms.javabase;

/**
 * @ClassName GCTest
 * @Description GC 测试
 * @Author daiyanping
 * @Date 2019-11-13
 * @Version 0.1
 */
public class GCTest {


    public static void main(String[] args) {
//        test();
        test2();
    }

    /**
     * 执行gc后，placeHolder并没有被回收，虽然已经出了作用域
     *
     * public static void test();
     *     descriptor: ()V
     *     flags: ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=3, locals=1, args_size=0
     *          0: ldc           #3                  // int 67108864
     *          2: newarray       byte
     *          4: astore_0
     *          5: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *          8: aload_0
     *          9: arraylength
     *         10: sipush        1024
     *         13: idiv
     *         14: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V
     *         17: invokestatic  #6                  // Method java/lang/System.gc:()V
     *         20: return
     *       LineNumberTable:
     *         line 22: 0
     *         line 23: 5
     *         line 25: 17
     *         line 26: 20
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             5      12     0 placeHolder   [B
     *
     *
     *  gc时placeHolder没有被回收是因为存在局部变量对其引用，而且这个局部变量是在编译时就已经确定（LocalVariableTable中）。
     *  jvm提供的栈上优化就是当我们再提供一个局部变量时，出了作用域后，如果发现之前的局部变量没有被使用
     *  就在LocalVariableTable将其覆盖，示例test2
     */
    public static void test() {
        if (true) {
            byte[] placeHolder = new byte[64 * 1024 * 1024];
            System.out.println(placeHolder.length / 1024);
        }
        System.gc();
    }

    /**
     * 执行gc后，手动将placeHolder置空，placeHolder被回收
     * public static void test1();
     *     descriptor: ()V
     *     flags: ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=3, locals=1, args_size=0
     *          0: ldc           #3                  // int 67108864
     *          2: newarray       byte
     *          4: astore_0
     *          5: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *          8: aload_0
     *          9: arraylength
     *         10: sipush        1024
     *         13: idiv
     *         14: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V
     *         17: aconst_null
     *         18: astore_0
     *         19: invokestatic  #6                  // Method java/lang/System.gc:()V
     *         22: return
     *       LineNumberTable:
     *         line 33: 0
     *         line 34: 5
     *         line 35: 17
     *         line 37: 19
     *         line 38: 22
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             5      14     0 placeHolder   [B
     */
    public static void test1() {
        if (true) {
            byte[] placeHolder = new byte[64 * 1024 * 1024];
            System.out.println(placeHolder.length / 1024);
            placeHolder = null;
        }
        System.gc();
    }

    /**
     * public static void test2();
     *     descriptor: ()V
     *     flags: ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=3, locals=1, args_size=0
     *          0: ldc           #3                  // int 67108864
     *          2: newarray       byte
     *          4: astore_0
     *          5: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *          8: aload_0
     *          9: arraylength
     *         10: sipush        1024
     *         13: idiv
     *         14: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V
     *         17: iconst_1
     *         18: istore_0
     *         19: invokestatic  #6                  // Method java/lang/System.gc:()V
     *         22: return
     *       LineNumberTable:
     *         line 99: 0
     *         line 100: 5
     *         line 104: 17
     *         line 105: 19
     *         line 106: 22
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             5      12     0 placeHolder   [B
     *            19       4     0 replacer   I
     *
     *  可以发现replacer的Solt和placeHolder的Slot是相同的
     *  也就是placeHolder被覆盖了,这样执行gc时placeHolder将被回收
     */
    public static void test2() {
        if (true) {
            byte[] placeHolder = new byte[64 * 1024 * 1024];
            System.out.println(placeHolder.length / 1024);

        }

        int replacer = 1;
        System.gc();
    }

}
