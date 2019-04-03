package com.daiyanping.cms.DB;

import javax.validation.executable.ValidateOnExecution;
import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DB {

    DBTypeEnum DB() default DBTypeEnum.TEST;
}
