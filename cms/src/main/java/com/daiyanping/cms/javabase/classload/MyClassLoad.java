package com.daiyanping.cms.javabase.classload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoad extends ClassLoader {

    private String classPath;

    public MyClassLoad(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] date = new byte[0];
        try {
            date = loadByte(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defineClass(name, date, 0, date.length);
    }

    private byte[] loadByte(String name) throws Exception {
        String fileName = name.replaceAll("\\.", "/");
        FileInputStream fileInputStream = new FileInputStream(classPath + File.separator + fileName + ".class");
        int available = fileInputStream.available();
        byte[] bytes = new byte[available];
        fileInputStream.read(bytes);
        fileInputStream.close();
        return bytes;

    }

    public static void main(String[] args) throws Exception {
        MyClassLoad myClassLoad = new MyClassLoad("D://test");
        // 默认走的APPClassLoad，删了target目录的class后就由MyClassLoad去加载·
        Class<?> delayDoubleDelete = myClassLoad.loadClass("com.daiyanping.cms.annotation.DelayDoubleDelete");
//        Object o = delayDoubleDelete.newInstance();
        ClassLoader classLoader = delayDoubleDelete.getClassLoader();
        System.out.println(classLoader);
    }
}
