package com.daiyanping.springcloud.common.DB;

/**
 * @ClassName AopProxyContext
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-04
 * @Version 0.1
 */
public class AopProxyContext {

    private static ThreadLocal<Object> aopProxy = new ThreadLocal<Object>();

    public static void setAopProxy(Object object) {
        aopProxy.set(object);
    }

    public static Object getAopProxy() {
        return aopProxy.get();
    }

    public static void clean() {
        aopProxy.remove();
    }
}
