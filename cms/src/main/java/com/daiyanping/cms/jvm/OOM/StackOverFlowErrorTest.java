package com.daiyanping.cms.jvm.OOM;

public class StackOverFlowErrorTest {

    /**
     * 递归调用导致线程栈内存溢出
     * 抛出StackOverflowError
     * @param args
     */
    public static void main(String[] args) {
        test();
    }
    public static void test() {
        test();
    }
}
