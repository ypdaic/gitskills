package com.daiyanping.cms.annotation;

import java.lang.annotation.*;

/**
 * @ClassName Resubmit
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
@Inherited
@Documented
public @interface Resubmit {

    long resuInterval() default  20l;

    String key() default "";
}
