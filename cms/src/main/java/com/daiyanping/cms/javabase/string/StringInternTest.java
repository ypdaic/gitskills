package com.daiyanping.cms.javabase.string;

/**
 * 如何保证变量s指向的是字符串常量池中的数据呢
 * 有两种方式：
 * 方式一： String s = "hello"
 *
 * 方式二：String s = new String("hello").intern();
 *
 * Sring.intern 方法 总结:
 *
 * 1.6 如果池中有，则并不会放入，返回已有对象的地址，如果没有，会把此对象复制一份，放入池中，并返回池中的对象地址
 *
 * 1.7起 如果池中有，则并不会放入，返回已有对象的地址，如果 没有，则会把对象的引用地址复制一份，放入池中，并返回池中的
 * 引用地址
 */
public class StringInternTest {

    /**
     *   0 new #2 <java/lang/String>
     *  3 dup
     *  4 ldc #3 <ab>
     *  6 invokespecial #4 <java/lang/String.<init>>
     *  9 astore_1
     * 10 new #5 <java/lang/StringBuilder>
     * 13 dup
     * 14 invokespecial #6 <java/lang/StringBuilder.<init>>
     * 17 new #2 <java/lang/String>
     * 20 dup
     * 21 ldc #7 <a>
     * 23 invokespecial #4 <java/lang/String.<init>>
     * 26 invokevirtual #8 <java/lang/StringBuilder.append>
     * 29 new #2 <java/lang/String>
     * 32 dup
     * 33 ldc #9 <b>
     * 35 invokespecial #4 <java/lang/String.<init>>
     * 38 invokevirtual #8 <java/lang/StringBuilder.append>
     * 41 invokevirtual #10 <java/lang/StringBuilder.toString>
     * 44 astore_2
     * 45 return
     * @param args
     */
    public static void main(String[] args) {
        // 这里会有两个对象，一个堆中的string对象，一个常量池中的ab对象
//        String ab = new String("ab");

        // 这里会有2个String对象，一个StringBuilder对象，常量池a, b
        String s = new String("a") + new String("b"); // s引用的堆中的string
        s.intern(); // 字符串常量中没用ab，所以生成ab
        String c = "ab"; // 这里引用常量池中上面生成的ab
        System.out.println(s == c); // jdk6 结果为false，原因堆和永久代各一份
                                    // jdk7 以上为true，原因常量池在堆中，为节省空间，常量池就直接引用堆中的对象
    }
}


