package com.daiyanping.cms.cache;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 缓存锁基础支持
 * @author daiyanping
 */
public abstract class AbstractCacheLockInterceptor extends AbstractCacheInterceptor {

    @Override
    protected boolean matchMethod(Method method) {
        if (method.getName().equals("get") && method.getParameterCount() == 2) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes[0].isAssignableFrom(Object.class) && parameterTypes[0].isAssignableFrom(Callable.class)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Object invokeMethod(MethodInvocation invocation) throws Throwable {
        Object[] objects = invocation.getArguments();
        return get(objects[0], (Callable<T>)objects[1], invocation);
    }

    public abstract <T> T get(Object key, Callable<T> valueLoader, MethodInvocation invocation) throws Throwable;

}
