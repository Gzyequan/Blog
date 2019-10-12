package com.yequan.validation.validator;

import org.apache.commons.lang3.time.DateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.Date;

/**
 * @Auther: yq
 * @Date: 2019/10/12 10:13
 * @Description:
 */
public class DateConstraintValidator implements ConstraintValidator<com.yequan.validation.annotation.DateValidator, String> {

    private String dateFormat;

    @Override
    public void initialize(com.yequan.validation.annotation.DateValidator constraintAnnotation) {
        dateFormat = constraintAnnotation.dateFormat();
    }

    /**
     * 校验
     *
     * @param value   需要校验的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || "".equals(value)) {
            return true;
        }
        try {
            Date date = DateUtils.parseDate(value, dateFormat);
            return null != date;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
