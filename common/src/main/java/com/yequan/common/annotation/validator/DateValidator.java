package com.yequan.common.annotation.validator;

import com.yequan.common.util.Logger;
import org.apache.commons.lang3.time.DateUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.text.ParseException;
import java.util.Date;

/**
 * @Auther: yq
 * @Date: 2019/9/4 11:24
 * @Description: 日期校验注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//自定义校验注解必须使用该注解进行注解
@Constraint(validatedBy = {DateValidator.DateValidatorInner.class})
public @interface DateValidator {

    /**
     * 校验信息:不允许为空
     *
     * @return
     */
    String message() default "日期格式不匹配,正确:{dateFormat}";

    /**
     * 校验规则
     *
     * @return
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DateValidatorInner implements ConstraintValidator<DateValidator, String> {

        private String dateFormat;

        @Override
        public void initialize(DateValidator constraintAnnotation) {
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
                Logger.error(e.getMessage(), e);
                return false;
            }
        }
    }

}
