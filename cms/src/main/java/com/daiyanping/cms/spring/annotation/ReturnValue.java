package com.daiyanping.cms.spring.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ReturnValue {
    String name() default "";
}
