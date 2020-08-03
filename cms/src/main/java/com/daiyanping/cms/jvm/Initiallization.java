package com.daiyanping.cms.jvm;

/**
 * @ClassName Initiallization
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/7/31
 * @Version 0.1
 */
public class Initiallization {

    /**
     * 通过jvm 参数可以查看是否能够导致子类加载  -XX:+TraceClassLoading
     * @param args
     */
    public static void main(String[] args) {
        Initiallization initiallization = new Initiallization();
//        initiallization.m1(); //打印子类的静态字段
//        initiallization.m2(); //使用数组的方式创建
        initiallization.m3(); //打印一个常量
//        initiallization.m4(); //如果使用常量去引用另外一个常量
    }

    public void m1() {
        // 如果通过子类引用父类的静态字段，只会触发父类的初始化，而不会触发子类的初始化(但是子类会被加载)
        System.out.println(SubClazz.value);
    }

    public void m2() {
        // 使用数组的方式，SuperClazz会被加载，但是不会触发初始化（同样不会触发子类加载）
        SuperClazz[] superClazzes = new SuperClazz[10];
    }

    public void m3() {
        // 打印一个常量，不会触发初始化（同样不会触发类加载）
        // 为什么不会触发类加载，因为在编译的时候，常量数据已经进入自己类的常量池
        System.out.println(SuperClazz.HELLOWORLD);
    }

    public void m4() {
        // 如果使用常量去引用另外一个常量（这个值未知，所以必须要触发初始化）
        System.out.println(SuperClazz.WHAT);
    }



}
