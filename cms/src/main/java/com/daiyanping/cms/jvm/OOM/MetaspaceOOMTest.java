package com.daiyanping.cms.jvm.OOM;

import com.google.gson.internal.$Gson$Preconditions;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MetaspaceOOMTest {

    /**
     * -XX:MetaspaceSize=30m -XX:MaxMetaspaceSize=30m
     * Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
     * @param args
     */
    public static void main(String[] args) {
        int i = 0; // 模拟计数多少次以后发生异常

        try {
             while (true) {
                 i++;
                 Enhancer enhancer = new Enhancer();
                 enhancer.setSuperclass(OOMTest.class);
                 enhancer.setUseCache(false);
                 enhancer.setCallback(new MethodInterceptor() {
                     @Override
                     public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                         return null;
                     }
                 });
                 enhancer.create();
             }
        } catch (Throwable e) {
            System.out.println("*************多少次后发生了异常：" + i);
            e.printStackTrace();
        }
    }


    public static class OOMTest {

    }
}
