package com.daiyanping.cms.javabase;

import org.junit.Test;

/**
 * @ClassName StringTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-11
 * @Version 0.1
 */
public class StringTest {

    @Test
    public void test1() {
        String str ="y" ;
        str= str+"w"+"e";
        str = str +"w"+"e"+"i";
        System.out.println(str);
    }

    /**
     * Classfile /Users/daiyanping/git-clone-repository/gitskills/cms/target/test-classes/com/daiyanping/cms/javabase/StringTest.class
     *   Last modified 2019-9-11; size 838 bytes
     *   MD5 checksum ca34596453dbfc2a6673fb06f6cb6a44
     *   Compiled from "StringTest.java"
     * public class com.daiyanping.cms.javabase.StringTest
     *   minor version: 0
     *   major version: 52
     *   flags: ACC_PUBLIC, ACC_SUPER
     * Constant pool:
     *    #1 = Methodref          #12.#27        // java/lang/Object."<init>":()V
     *    #2 = String             #28            // y
     *    #3 = Class              #29            // java/lang/StringBuilder
     *    #4 = Methodref          #3.#27         // java/lang/StringBuilder."<init>":()V
     *    #5 = Methodref          #3.#30         // java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *    #6 = String             #31            // we
     *    #7 = Methodref          #3.#32         // java/lang/StringBuilder.toString:()Ljava/lang/String;
     *    #8 = String             #33            // wei
     *    #9 = Fieldref           #34.#35        // java/lang/System.out:Ljava/io/PrintStream;
     *   #10 = Methodref          #36.#37        // java/io/PrintStream.println:(Ljava/lang/String;)V
     *   #11 = Class              #38            // com/daiyanping/cms/javabase/StringTest
     *   #12 = Class              #39            // java/lang/Object
     *   #13 = Utf8               <init>
     *   #14 = Utf8               ()V
     *   #15 = Utf8               Code
     *   #16 = Utf8               LineNumberTable
     *   #17 = Utf8               LocalVariableTable
     *   #18 = Utf8               this
     *   #19 = Utf8               Lcom/daiyanping/cms/javabase/StringTest;
     *   #20 = Utf8               test1
     *   #21 = Utf8               str
     *   #22 = Utf8               Ljava/lang/String;
     *   #23 = Utf8               RuntimeVisibleAnnotations
     *   #24 = Utf8               Lorg/junit/Test;
     *   #25 = Utf8               SourceFile
     *   #26 = Utf8               StringTest.java
     *   #27 = NameAndType        #13:#14        // "<init>":()V
     *   #28 = Utf8               y
     *   #29 = Utf8               java/lang/StringBuilder
     *   #30 = NameAndType        #40:#41        // append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *   #31 = Utf8               we
     *   #32 = NameAndType        #42:#43        // toString:()Ljava/lang/String;
     *   #33 = Utf8               wei
     *   #34 = Class              #44            // java/lang/System
     *   #35 = NameAndType        #45:#46        // out:Ljava/io/PrintStream;
     *   #36 = Class              #47            // java/io/PrintStream
     *   #37 = NameAndType        #48:#49        // println:(Ljava/lang/String;)V
     *   #38 = Utf8               com/daiyanping/cms/javabase/StringTest
     *   #39 = Utf8               java/lang/Object
     *   #40 = Utf8               append
     *   #41 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
     *   #42 = Utf8               toString
     *   #43 = Utf8               ()Ljava/lang/String;
     *   #44 = Utf8               java/lang/System
     *   #45 = Utf8               out
     *   #46 = Utf8               Ljava/io/PrintStream;
     *   #47 = Utf8               java/io/PrintStream
     *   #48 = Utf8               println
     *   #49 = Utf8               (Ljava/lang/String;)V
     * {
     *   public com.daiyanping.cms.javabase.StringTest();
     *     descriptor: ()V
     *     flags: ACC_PUBLIC
     *     Code:
     *       stack=1, locals=1, args_size=1
     *          0: aload_0
     *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *          4: return
     *       LineNumberTable:
     *         line 12: 0
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       5     0  this   Lcom/daiyanping/cms/javabase/StringTest;
     *
     *   public void test1();
     *     descriptor: ()V
     *     flags: ACC_PUBLIC
     *     Code:
     *       stack=2, locals=2, args_size=1
     *          0: ldc           #2                  // String y   	常量池中的常量值（int, float, string reference, object reference）入栈。
     *          2: astore_1                                         将栈顶引用类型值保存到局部变量1中。
     *          3: new           #3                  // class java/lang/StringBuilder   创建新的对象实例。
     *          6: dup                               复制栈顶一个字长的数据，将复制后的数据压栈。
     *          7: invokespecial #4                  // Method java/lang/StringBuilder."<init>":()V    编译时方法绑定调用方法。
     *         10: aload_1                           从局部变量1中装载引用类型值入栈。
     *         11: invokevirtual #5                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder; 	运行时方法绑定调用方法
     *         14: ldc           #6                  // String we
     *         16: invokevirtual #5                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *         19: invokevirtual #7                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
     *         22: astore_1
     *         23: new           #3                  // class java/lang/StringBuilder
     *         26: dup
     *         27: invokespecial #4                  // Method java/lang/StringBuilder."<init>":()V
     *         30: aload_1
     *         31: invokevirtual #5                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *         34: ldc           #8                  // String wei
     *         36: invokevirtual #5                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *         39: invokevirtual #7                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
     *         42: astore_1
     *         43: getstatic     #9                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *         46: aload_1
     *         47: invokevirtual #10                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     *         50: return
     *       LineNumberTable:
     *         line 16: 0
     *         line 17: 3
     *         line 18: 23
     *         line 19: 43
     *         line 20: 50
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0      51     0  this   Lcom/daiyanping/cms/javabase/StringTest;
     *             3      48     1   str   Ljava/lang/String;
     *     RuntimeVisibleAnnotations:
     *       0: #24()
     * }
     * SourceFile: "StringTest.java"
     */
}
