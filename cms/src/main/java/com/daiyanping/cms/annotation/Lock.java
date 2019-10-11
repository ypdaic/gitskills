package com.daiyanping.cms.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
@Inherited
@Documented
public @interface Lock {

    String key() default "";
}
