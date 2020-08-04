package com.daiyanping.cms.javabase.classload;

public class ClassLoaderTest {

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
        // 获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2

        // 获取当前线程的classLoader
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2

        ClassLoader parent = systemClassLoader.getParent();
        System.out.println(parent); //sun.misc.Launcher$ExtClassLoader@182decdb

        // 获取不到引导类加载器
        ClassLoader bootstrapClassLoader = parent.getParent();
        System.out.println(bootstrapClassLoader); //null

        // 获取用户自定义类的类加载器, 默认使用系统类加载器进行加载
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2

        // String类使用引导类加载器进行加载的， ------》java 核心类库都是使用引导类加载器进行加载的
        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1); //null
    }
}
