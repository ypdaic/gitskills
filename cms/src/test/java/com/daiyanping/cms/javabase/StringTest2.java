package com.daiyanping.cms.javabase;

import org.junit.Test;

/**
 * @ClassName StringTest2
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-11
 * @Version 0.1
 */
public class StringTest2 {

    @Test
    public static void test() {
        String str ="y" ;
        String s = "y"+"w"+"e";
        str= str+"w"+"e";
        System.out.println(s==str);
        str = str +"w"+"e"+"i";
    }

//    Classfile /Users/daiyanping/git-clone-repository/gitskills/cms/target/test-classes/com/daiyanping/cms/javabase/StringTest2.class
//    Last modified 2019-9-11; size 934 bytes
//    MD5 checksum 7dd2e2ec39925a3a767f09d461194755
//    Compiled from "StringTest2.java"
//    public class com.daiyanping.cms.javabase.StringTest2
//    minor version: 0
//    major version: 52
//    flags: ACC_PUBLIC, ACC_SUPER
//    Constant pool:
//            #1 = Methodref          #13.#32        // java/lang/Object."<init>":()V
//            #2 = String             #33            // y
//            #3 = String             #34            // ywe
//            #4 = Class              #35            // java/lang/StringBuilder
//            #5 = Methodref          #4.#32         // java/lang/StringBuilder."<init>":()V
//            #6 = Methodref          #4.#36         // java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
//            #7 = String             #37            // we
//            #8 = Methodref          #4.#38         // java/lang/StringBuilder.toString:()Ljava/lang/String;
//            #9 = Fieldref           #39.#40        // java/lang/System.out:Ljava/io/PrintStream;
//            #10 = Methodref          #41.#42        // java/io/PrintStream.println:(Z)V
//            #11 = String             #43            // wei
//            #12 = Class              #44            // com/daiyanping/cms/javabase/StringTest2
//            #13 = Class              #45            // java/lang/Object
//            #14 = Utf8               <init>
//  #15 = Utf8               ()V
//  #16 = Utf8               Code
//  #17 = Utf8               LineNumberTable
//  #18 = Utf8               LocalVariableTable
//  #19 = Utf8               this
//            #20 = Utf8               Lcom/daiyanping/cms/javabase/StringTest2;
//  #21 = Utf8               test
//  #22 = Utf8               str
//  #23 = Utf8               Ljava/lang/String;
//  #24 = Utf8               s
//  #25 = Utf8               StackMapTable
//  #26 = Class              #46            // java/lang/String
//            #27 = Class              #47            // java/io/PrintStream
//            #28 = Utf8               RuntimeVisibleAnnotations
//  #29 = Utf8               Lorg/junit/Test;
//  #30 = Utf8               SourceFile
//  #31 = Utf8               StringTest2.java
//  #32 = NameAndType        #14:#15        // "<init>":()V
//            #33 = Utf8               y
//  #34 = Utf8               ywe
//  #35 = Utf8               java/lang/StringBuilder
//  #36 = NameAndType        #48:#49        // append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
//            #37 = Utf8               we
//  #38 = NameAndType        #50:#51        // toString:()Ljava/lang/String;
//            #39 = Class              #52            // java/lang/System
//            #40 = NameAndType        #53:#54        // out:Ljava/io/PrintStream;
//            #41 = Class              #47            // java/io/PrintStream
//            #42 = NameAndType        #55:#56        // println:(Z)V
//            #43 = Utf8               wei
//  #44 = Utf8               com/daiyanping/cms/javabase/StringTest2
//  #45 = Utf8               java/lang/Object
//  #46 = Utf8               java/lang/String
//  #47 = Utf8               java/io/PrintStream
//  #48 = Utf8               append
//  #49 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
//  #50 = Utf8               toString
//  #51 = Utf8               ()Ljava/lang/String;
//  #52 = Utf8               java/lang/System
//  #53 = Utf8               out
//  #54 = Utf8               Ljava/io/PrintStream;
//  #55 = Utf8               println
//  #56 = Utf8               (Z)V
//    {
//  public com.daiyanping.cms.javabase.StringTest2();
//        descriptor: ()V
//        flags: ACC_PUBLIC
//        Code:
//        stack=1, locals=1, args_size=1
//        0: aload_0
//        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
//        4: return
//            LineNumberTable:
//        line 12: 0
//        LocalVariableTable:
//        Start  Length  Slot  Name   Signature
//        0       5     0  this   Lcom/daiyanping/cms/javabase/StringTest2;
//
//        public static void test();
//        descriptor: ()V
//        flags: ACC_PUBLIC, ACC_STATIC
//        Code:
//        stack=3, locals=2, args_size=0
//        0: ldc           #2                  // String y
//        2: astore_0
//        3: ldc           #3                  // String ywe
//        5: astore_1
//        6: new           #4                  // class java/lang/StringBuilder
//        9: dup
//        10: invokespecial #5                  // Method java/lang/StringBuilder."<init>":()V
//        13: aload_0
//        14: invokevirtual #6                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
//        17: ldc           #7                  // String we
//        19: invokevirtual #6                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
//        22: invokevirtual #8                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
//        25: astore_0
//        26: getstatic     #9                  // Field java/lang/System.out:Ljava/io/PrintStream;
//        29: aload_1
//        30: aload_0
//        31: if_acmpne     38
//        34: iconst_1
//        35: goto          39
//        38: iconst_0
//        39: invokevirtual #10                 // Method java/io/PrintStream.println:(Z)V
//        42: new           #4                  // class java/lang/StringBuilder
//        45: dup
//        46: invokespecial #5                  // Method java/lang/StringBuilder."<init>":()V
//        49: aload_0
//        50: invokevirtual #6                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
//        53: ldc           #11                 // String wei
//        55: invokevirtual #6                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
//        58: invokevirtual #8                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
//        61: astore_0
//        62: return
//            LineNumberTable:
//        line 16: 0
//        line 17: 3
//        line 18: 6
//        line 19: 26
//        line 20: 42
//        line 21: 62
//        LocalVariableTable:
//        Start  Length  Slot  Name   Signature
//        3      60     0   str   Ljava/lang/String;
//        6      57     1     s   Ljava/lang/String;
//        StackMapTable: number_of_entries = 2
//        frame_type = 255 /* full_frame */
//        offset_delta = 38
//        locals = [ class java/lang/String, class java/lang/String ]
//        stack = [ class java/io/PrintStream ]
//        frame_type = 255 /* full_frame */
//        offset_delta = 0
//        locals = [ class java/lang/String, class java/lang/String ]
//        stack = [ class java/io/PrintStream, int ]
//        RuntimeVisibleAnnotations:
//        0: #29()
//    }
//    SourceFile: "StringTest2.java"

}
