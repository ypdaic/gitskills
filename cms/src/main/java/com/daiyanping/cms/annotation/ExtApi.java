package com.daiyanping.cms.annotation;

import com.daiyanping.cms.enums.ExtApiTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD, ElementType.TYPE
})
@Inherited
@Documented
public @interface ExtApi {

	@AliasFor("value")
	ExtApiTypeEnum type() default ExtApiTypeEnum.CMS_SIGN;

	@AliasFor("type")
	ExtApiTypeEnum value() default ExtApiTypeEnum.CMS_SIGN;

}
