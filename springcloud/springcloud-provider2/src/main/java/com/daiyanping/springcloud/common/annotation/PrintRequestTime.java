package com.daiyanping.springcloud.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD, ElementType.TYPE
})
@Inherited
@Documented
public @interface PrintRequestTime {

    /**
     * 打印error日志的最小时间 ms
     * @return
     */
    long printError() default 3000l;

    /**
     * 打印info日志的最小时间 ms
     * @return
     */
    long printInfo() default 3000l;

    /**
     * 默认打印error 日志
     * @return
     */
    boolean isError() default true;
}
