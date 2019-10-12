package com.yequan.validation.annotation;

import com.yequan.constant.PermissionConsts;
import com.yequan.validation.validator.PermissionTypeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Auther: yq
 * @Date: 2019/9/4 16:04
 * @Description: 权限类型校验, 只能是指定类型
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PermissionTypeConstraintValidator.class})
public @interface PermissionTypeValidator {

    String message() default "权限类型错误,正确:{types}";

    String types() default PermissionConsts.PERMISSION_TYPES;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
