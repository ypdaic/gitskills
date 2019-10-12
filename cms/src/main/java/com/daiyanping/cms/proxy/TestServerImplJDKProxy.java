package com.daiyanping.cms.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName TestServerImplProxy
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@AllArgsConstructor
@Data
public class TestServerImplJDKProxy implements InvocationHandler {
    private TestServiceImpl testService;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
