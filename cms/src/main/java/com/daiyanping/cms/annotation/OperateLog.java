package com.daiyanping.cms.annotation;

import java.lang.annotation.*;

/**
 * @ClassName OperateLog
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-16
 * @Version 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
@Inherited
@Documented
public @interface OperateLog {
    String message() default "";

    String accountInfo() ;
}
