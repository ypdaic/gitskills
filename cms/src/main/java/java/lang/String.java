package java.lang;

import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;

public class String {

    /**
     * 这里的String 依旧加载的是jdk的，所以就报了如下错误
     *
     * 错误: 在类 java.lang.String 中找不到 main 方法, 请将 main 方法定义为:
     *    public static void main(String[] args)
     * 否则 JavaFX 应用程序类必须扩展javafx.application.Application
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("hello");
    }
}
