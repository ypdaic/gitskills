package com.daiyanping.cms.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 缓存代理aop支持
 * @author daiyanping
 */
public abstract class AbstractCacheInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (matchMethod(invocation.getMethod())) {
            return invokeMethod(invocation);
        }
        return invocation.proceed();
    }

    /**
     * 需要拦截的方法
     * @return
     */
    protected abstract boolean matchMethod(Method method);

    /**
     * 执行aop
     * @param invocation
     * @return
     */
    protected abstract Object invokeMethod(MethodInvocation invocation) throws Throwable;
}
