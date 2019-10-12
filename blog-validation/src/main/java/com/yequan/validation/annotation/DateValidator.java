package com.yequan.validation.annotation;

import com.yequan.validation.validator.DateConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Auther: yq
 * @Date: 2019/9/4 11:24
 * @Description: 日期校验注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//自定义校验注解必须使用该注解进行注解
@Constraint(validatedBy = {DateConstraintValidator.class})
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

}
