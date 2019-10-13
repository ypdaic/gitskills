package com.daiyanping.cms.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName TestServerImplCglibProxy
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@AllArgsConstructor
@Data
public class TestServerImplCglibProxy implements MethodInterceptor {

    private TestServiceImpl testService;

    /**
     * o 生成的代理对象,objects 方法参数, method 方法,methodProxy 代理类中的方法
     * @param o
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().equals("say2")) {
            System.out.println("sdfsdf");
            return "sdfs";
        }
        Object invoke = method.invoke(testService, objects);
        return invoke;
    }
}
