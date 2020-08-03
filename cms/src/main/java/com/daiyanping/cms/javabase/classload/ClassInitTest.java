package com.daiyanping.cms.javabase.classload;

public class ClassInitTest {

    private static int num = 1;

    static {
        num = 2;
        number = 10;
    }

    private static int number = 10;

    /**
     *  <client> 方法的字节码
     *  0 iconst_1
     *  1 putstatic #3 <com/daiyanping/cms/javabase/classload/ClassInitTest.num>
     *  4 iconst_2
     *  5 putstatic #3 <com/daiyanping/cms/javabase/classload/ClassInitTest.num>
     *  8 bipush 10
     * 10 putstatic #5 <com/daiyanping/cms/javabase/classload/ClassInitTest.number>
     * 13 bipush 10
     * 15 putstatic #5 <com/daiyanping/cms/javabase/classload/ClassInitTest.number>
     * 18 return
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(ClassInitTest.num);
        System.out.println(ClassInitTest.number);
    }
}
