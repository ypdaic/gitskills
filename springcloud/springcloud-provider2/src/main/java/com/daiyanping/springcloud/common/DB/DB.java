package com.daiyanping.springcloud.common.DB;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DB {

    DBTypeEnum value() default DBTypeEnum.DB1;
}
