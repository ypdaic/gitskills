package com.daiyanping.cms.annotation;

import com.daiyanping.cms.validator.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @ClassName IdCard
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-16
 * @Version 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.PARAMETER, ElementType.FIELD
})
@Inherited
@Documented
@Constraint(validatedBy = IdCardValidator.class)
public @interface IdCard {

    String message() default "身份证号码不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
