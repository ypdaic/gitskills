package com.daiyanping.cms;

import com.daiyanping.cms.functionInterface.MyFunctionInterface;
import com.daiyanping.cms.functionInterface.MyFunctionInterface2;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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

        List<String> al = Arrays.asList("a", "b", "c", "d");
        al.forEach(MyFunctionInterfaceTests::printValur);
//        //下面的方法和上面等价的
        Consumer<String> printValur = MyFunctionInterfaceTests::printValur;//方法参数
        al.forEach(x -> printValur.accept(x));//方法执行accept

    }

    private void test2(MyFunctionInterface myFunctionInterface) {
        myFunctionInterface.say("我是函数式接口");
    }

    private void test3(MyFunctionInterface2 myFunctionInterface) {
        myFunctionInterface.say();
    }

    public static void  printValur(String str){
        System.out.println("print value : "+str);
    }

}
