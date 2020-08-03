package com.daiyanping.cms.javabase.classload;

public class ClassInit3Test {

    private int num = 1;

    /**
     * <init> 方法字节码
     *  0 aload_0
     *  1 invokespecial #1 <java/lang/Object.<init>>
     *  4 aload_0
     *  5 iconst_1
     *  6 putfield #2 <com/daiyanping/cms/javabase/classload/ClassInit3Test.num>
     *  9 aload_0
     * 10 iconst_3
     * 11 putfield #2 <com/daiyanping/cms/javabase/classload/ClassInit3Test.num>
     * 14 return
     * @param num
     */
    public ClassInit3Test(int num) {
        this.num = 3;
    }

    /**
     * 此时不存在<client>方法，但是存在<init>方法，也就是默认的构造器方法
     * @param args
     */
    public static void main(String[] args) {
        int b = 2;
    }
}
