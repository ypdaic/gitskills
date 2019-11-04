package com.daiyanping.cms.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
@Inherited
@Documented
public @interface RetryCountCheck {

    /**
     * 重试次数
     * @return
     */
    int count() default 10;

    /**
     * 缓存时间秒
     * @return
     */
    long cacheTime() default 3600l;

    /**
     * 缓存key
     * @return
     */
    String key() default "";

    /**
     * 缓存的名称
     * @return
     */
    String cacheName() default "";
}
