package com.daiyanping.cms.javabase.string;

/**
 * G1的String 去重操作，去重是对String的char[] 数组进行去重
 * -XX:+UseStringDeduplication -XX:+UseG1GC
 */
public class StringTest3 {

    public static void main(String[] args) {
        String s = new String("1");
        String s1 = new String("1");

        System.out.println(s == s1); // 这里始终是false
    }
}
