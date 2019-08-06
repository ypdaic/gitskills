package com.daiyanping.cms.mymvc.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyQualifier {

    String value() default "";
}
