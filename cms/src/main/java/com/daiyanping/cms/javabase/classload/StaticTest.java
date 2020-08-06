package com.daiyanping.cms.javabase.classload;

/**
 * @ClassName StaticTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/8/4
 * @Version 0.1
 */
public class StaticTest {

    public static void main(String[] args) {
        /**
         0 new #2 <com/daiyanping/cms/javabase/classload/StaticTest>
         3 dup
         4 invokespecial #3 <com/daiyanping/cms/javabase/classload/StaticTest.<init>>  invokespecial 用于调用私有实例方法、构造器及 super 关键字等
         7 astore_1
         8 aload_1
         9 invokespecial #4 <com/daiyanping/cms/javabase/classload/StaticTest.test>
         12 invokestatic #5 <com/daiyanping/cms/javabase/classload/StaticTest.hello>   invokestatic 用来调用静态方法
         15 return

         */
        StaticTest staticTest = new StaticTest();
        staticTest.test();
        hello();
    }

    /**
     * 静态方法
     */
    public static void hello() {

        /**
         *
         * 非私有实例方法的调用
         *
         *
         * 0 getstatic #3 <java/lang/System.out>
         * 3 ldc #4 <hello>
         * 5 invokevirtual #5 <java/io/PrintStream.println>
         * 8 return
         *
         * invokevirtual 用于调用非私有实例方法，比如 public 和 protected，大多数方法调用属于这一种；
         */
        System.out.println("hello");
    }

    /**
     * 私有实例方法
     */
    private void test() {

    }
}
