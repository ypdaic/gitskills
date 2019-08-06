package com.daiyanping.cms.mymvc.annotation;

import java.lang.annotation.*;

/**
 * @ClassName MyRequestMapping
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-06
 * @Version 0.1
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {

    String value() default "";
}
