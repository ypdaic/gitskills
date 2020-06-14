package com.daiyanping.cms.javabase;

import com.sun.nio.zipfs.ZipPath;

import java.util.HashMap;

public class Test {

    public static void main(String[] args) {
        System.out.println(HashMap.class.getClassLoader());

        System.out.println(ZipPath.class.getClassLoader());

        System.out.println(GCTest.class.getClassLoader());
    }
}
