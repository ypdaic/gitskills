package com.daiyanping.cms.spring.aop.cglib;

import org.springframework.cglib.proxy.FixedValue;

public class FixedValueIntercepter implements FixedValue {
    @Override
    public Object loadObject() throws Exception {
        System.out.println("==================loadObject value");
        return "my value 111";
    }
}
