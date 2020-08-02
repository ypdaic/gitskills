package com.daiyanping.cms.javabase.string;

import java.util.HashSet;

/**
 * 字符串常量池中是不会存储相同内容的字符串的
 *
 * String 的 String Pool 是一个固定大小的Hashtable,默认值大小长度是1009，如果
 * 放进String Pool的String 非常多，就会造成Hash冲突严重，从而导致链表会很长，而
 * 链表长了后直接会造成的影响就是当调用String.intern时性能会大幅下降
 *
 * 使用-XX:StringTableSize可设置StringTable的长度
 * 在jdk6中StringTable是固定的，就是1009的长度，所以如果常量池中的字符串过多就会导致
 * 效率下降很快,StringTableSize设置没有要求
 * 在jdk7中，StringTable的长度默认值是60013,StringTableSize设置没有要求
 * 在jdk8中，1009是可设置的最小值
 *
 * 在jdk6中，字符串常量池放在永久代中
 * 在jdk7中，就字符串常量池调整到了java堆中
 * 在jdk8中，字符串放在堆中
 */
public class StringTest2 {

    public static void main(String[] args) {
        test1();
    }

    /**
     *
     0: ldc           #3                  // String abc  // String abc  将int、float或String型常量值从常量池中推送至栈顶
     2: astore_0                          将栈顶数值（objectref）存入当前frame的局部变量数组中指定下标 ndex 处的变量中，栈顶数值出栈
     3: ldc           #3                  // String abc
     5: astore_1
     6: ldc           #4                  // String hello
     8: astore_0
     9: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;   获取指定类的静态域，并将其值压入栈顶
     12: aload_0                          当前frame的局部变量数组中下标为 index 的引用型局部变量进栈
     13: aload_1
     14: if_acmpne     21
     17: iconst_1
     18: goto          22
     21: iconst_0
     22: invokevirtual #6                  // Method java/io/PrintStream.println:(Z)V
     25: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
     28: aload_0
     29: invokevirtual #7                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     32: new           #8                  // class java/lang/String
     35: dup
     36: ldc           #9                  // String edf
     38: invokespecial #10                 // Method java/lang/String."<init>":(Ljava/lang/String;)V
     41: astore_2
     42: aload_2
     43: invokevirtual #11                 // Method java/lang/String.intern:()Ljava/lang/String;
     46: pop
     47: return
     */
    public static void test1() {

        // 直接放在常量池
        String s1 = "abc";
        String s2 = "abc";
        s1 = "hello";

        System.out.println( s1 == s2);

        System.out.println(s1);

        String s3 = new String("edf");
        // 往常量池放入该字符串
        s3.intern();
        // 使用set保持着常量池引用，避免full gc回收常量池行为
        HashSet<String> set = new HashSet<>();

        int i = 0;
        while (true) {
           set.add(String.valueOf(i++).intern()) ;
        }
    }

}
