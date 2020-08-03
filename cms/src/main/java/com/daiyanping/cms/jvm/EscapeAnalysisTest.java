package com.daiyanping.cms.jvm;

/**
 * @ClassName EscapeAnalysisTest
 * @Description
 * @Author daiyanping
 * @Date 2020/7/24
 * @Version 0.1
 */
public class EscapeAnalysisTest {

    /**
     * 逃逸分析，栈上分配
     * 开启逃逸分析 -XX:+DoEscapeAnalysis ，默认开启
     * 关闭逃逸分析 -XX:-DoEscapeAnalysis
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 50000000; i++) {
            allocate();

        }
        System.out.println(System.currentTimeMillis() - start + "ms");
        Thread.sleep(600000);
    }

    /**
     * myObject 对象属于不可逃逸，JVM 可以在栈上分配
     */
    private static void allocate() {
        MyObject myObject = new MyObject(2020, 2002.6);
    }

    public static class MyObject {
        int a;
        double b;

        MyObject(int a, double b) {
            this.a = a;
            this.b = b;
        }
    }
}
