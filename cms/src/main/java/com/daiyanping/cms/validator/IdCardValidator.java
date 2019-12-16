package com.daiyanping.cms.validator;

import sungo.manager.annotation.IdCard;
import sungo.util.ValidateUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName IdCardValidator
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-16
 * @Version 0.1
 */
public class IdCardValidator implements ConstraintValidator<IdCard, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return ValidateUtil.isIDNumber(value.toString());
    }
}
