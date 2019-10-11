package com.daiyanping.cms.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
@Inherited
@Documented
public @interface DelayDoubleDelete {

    String key() default "";

    String keyGenerator() default "";

    /**
     * 是否支持事物
     * @return
     */
    boolean transactionAware() default false;

    /**
     * 缓存器名称,需要和springCache的一致
     * @return
     */
    String cacheNames() default "";
}
