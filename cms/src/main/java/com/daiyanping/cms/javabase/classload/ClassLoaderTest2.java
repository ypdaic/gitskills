package com.daiyanping.cms.javabase.classload;

import sun.applet.AppletClassLoader;
import sun.misc.Launcher;

import java.net.URL;

public class ClassLoaderTest2 {

    /**
     * 启动类加载器 Bootstrap ClassLoader
     *
     * 这个类加载器使用c/c++ 语言实现的，嵌套在JVM内部
     * 它用来加载java的核心库（JAVA_HOME/jre/lib/rt.jar resources.jar  sun.boot.class.path 路径下的内容） 用于提供
     * JVM自身需要的类
     *
     * 并不继承自java.lang.ClassLoader, 没有父加载器
     *
     * 加载器扩展类和应用程序类加载器，并指定为他们的父类加载器
     *
     * 出于安全考虑，Bootstrap启动类加载器只加载包名为java,javax,sun等开头的类
     * @param args
     */
    public static void main(String[] args) {

        /**
         * 获取BootstrapClassLoader 能够加载的路径
         */
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        for (URL urL : urLs) {

            /**
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/lib/resources.jar
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/lib/rt.jar
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/lib/sunrsasign.jar
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/lib/jsse.jar
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/lib/jce.jar
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/lib/charsets.jar
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/lib/jfr.jar
             * file:/D:/Program%20Files/Java/jdk1.8.0_181/jre/classes
             */
            System.out.println(urL.toExternalForm());
        }


        String property = System.getProperty("java.ext.dirs");
        String[] split = property.split(";");
        for (String s : split) {

            /**
             * 扩展类加载器
             * D:\Program Files\Java\jdk1.8.0_181\jre\lib\ext
             * C:\Windows\Sun\Java\lib\ext
             */
            System.out.println(s);

        }
    }
}
