package com.yequan.common.annotation.validator;

import com.yequan.common.application.constant.PermissionConsts;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/9/4 16:04
 * @Description: 权限类型校验, 只能是指定类型
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PermissionTypeValidator.PermissionTypeValidatorInner.class})
public @interface PermissionTypeValidator {

    String message() default "权限类型错误{types}";

    String types() default PermissionConsts.PERMISSION_TYPES;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PermissionTypeValidatorInner implements ConstraintValidator<PermissionTypeValidator, String> {

        private String types;

        @Override
        public void initialize(PermissionTypeValidator constraintAnnotation) {
            types = constraintAnnotation.types();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (null == value || "".equals(value)) {
                return true;
            }
            String[] typeArray = types.split(",");
            List<String> typeList = Arrays.asList(typeArray);
            return typeList.contains(value);
        }
    }

}
