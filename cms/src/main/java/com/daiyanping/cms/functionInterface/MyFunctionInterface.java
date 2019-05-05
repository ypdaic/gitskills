package com.daiyanping.cms.functionInterface;

/**
 * @ClassName MyFunctionInterface
 * @Description TODO 函数式接口只能有一个带实现的抽象方法
 * @Author daiyanping
 * @Date 2019-04-17
 * @Version 0.1
 */
@FunctionalInterface
public interface MyFunctionInterface<T> {

    /**
     * 带参数的方法
     * @param t
     * @throws MyException
     */
    void say(T t) throws MyException;

    class MyException extends RuntimeException {

    }

    boolean equals(Object obj);
}
