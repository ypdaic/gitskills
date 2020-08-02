package com.daiyanping.cms.javabase.string;

/**
 * String 不可变性
 */
public class StringTest1 {

    public static void main(String[] args) {
        test5();
    }

    /**
     *        0: ldc           #2                  // String abc
     *        2: astore_1
     *        3: ldc           #2                  // String abc
     *        5: astore_2
     *        6: ldc           #3                  // String hello
     *        8: astore_1
     */
    public static void test1() {
        String s1 = "abc";
        String s2 = "abc";
        s1 = "hello";

        System.out.println( s1 == s2);

        System.out.println(s1);
    }

    /**
     *        0: ldc           #3                  // String abc
     *        2: astore_0
     *        3: ldc           #3                  // String abc
     *        5: astore_1
     *        6: new           #8                  // class java/lang/StringBuilder
     *        9: dup
     *       10: invokespecial #9                  // Method java/lang/StringBuilder."<init>":()V
     *       13: aload_1
     *       14: invokevirtual #10                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *       17: ldc           #11                 // String def
     *       19: invokevirtual #10                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *       22: invokevirtual #12                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
     *       25: astore_1
     */
    public static void test2() {
        String s1 = "abc";
        String s2 = "abc";

        s2 += "def"; // 这是由StringBuilder.toString() 源码就是new String

        String s3 = "abcdef";
        System.out.println(s2 == s3); // 所以不相等，一个在字符串常量池，一个在堆中
        System.out.println(s1);
        System.out.println(s3);
    }
    public static void test3() {
        String s1 = "abc";
        // 这里不会产生字符串常量def ,而是abcdef
        String s2 = "abc" + "def";

//        s2 += "def"; // 这是由StringBuilder.toString() 源码就是new String

        String s3 = "abcdef";
        System.out.println(s2 == s3); // 所以不相等，一个在字符串常量池，一个在堆中
        System.out.println(s1);
        System.out.println(s3);
    }

    public static void test4() {
        String s1 = "javaEE";
        String s2 = "hadoop";
        String s3 = "javaEEhadoop";
        String s4 = "javaEE" + "hadoop"; // 编译器优化
        String s5 = s1 + "hadoop"; // 出现变量就会new StringBuilder
        String s6 = "javaEE" + s2;

        /**
         * s1 +s2 的执行细节
         * StringBuilder s = new StringBuilder();
         * s.append("javaEE");
         * s.append("hadoop");
         * s.toString() ---> 约等于new String("javaEEhadoop")
         */
        String s7 = s1 + s2;

        System.out.println(s3 == s4); // true
        System.out.println(s3 == s5); // false
        System.out.println(s3 == s6); // false
        System.out.println(s3 == s7); // false
        System.out.println(s5 == s6); // false
        System.out.println(s5 == s7); // false
        System.out.println(s6 == s7); // false

        String intern = s6.intern();
        System.out.println(intern == s3); // true

    }

    /**
     *  0 ldc #18 <a>
     *  2 astore_0
     *  3 ldc #19 <b>
     *  5 astore_1
     *  6 ldc #20 <ab>
     *  8 astore_2
     *  9 ldc #20 <ab>
     * 11 astore_3
     * 12 getstatic #5 <java/lang/System.out>
     * 15 aload_2
     * 16 aload_3
     * 17 if_acmpne 24 (+7)
     * 20 iconst_1
     * 21 goto 25 (+4)
     * 24 iconst_0
     * 25 invokevirtual #6 <java/io/PrintStream.println>
     * 28 retur
     */
    public static void test5() {

        // 这里是常量了，非变量
        final String a = "a";
        // 这里是常量了，非变量
        final String b = "b";
        String d = "ab";
        // 这里就还是常量
        String c = a + b;
        System.out.println(d == c); //true，这里又为true了
    }

}
