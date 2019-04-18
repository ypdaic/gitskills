package com.daiyanping.cms;

import com.daiyanping.cms.functionInterface.MyFunctionInterface;
import com.daiyanping.cms.functionInterface.MyFunctionInterface2;
import org.junit.Test;

/**
 * @ClassName MyFunctionInterfaceTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-18
 * @Version 0.1
 */
public class MyFunctionInterfaceTests {

    @Test
    public void test() {
        //带参数的函数式接口使用方式一
        test2((MyFunctionInterface) t -> {
            System.out.println(t);
        });
        //带参数的函数式接口使用方式二
        test2( t -> {
            System.out.println(t);
        });

        //不带参数的函数式接口使用方式二
        test3( () -> {
            System.out.println("我是函数式接口");
        });

    }

    private void test2(MyFunctionInterface myFunctionInterface) {
        myFunctionInterface.say("我是函数式接口");
    }

    private void test3(MyFunctionInterface2 myFunctionInterface) {
        myFunctionInterface.say();
    }


}
