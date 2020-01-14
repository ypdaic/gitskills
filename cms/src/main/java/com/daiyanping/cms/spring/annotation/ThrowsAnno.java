package com.daiyanping.cms.spring.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ThrowsAnno {
    String name() default "";
}
