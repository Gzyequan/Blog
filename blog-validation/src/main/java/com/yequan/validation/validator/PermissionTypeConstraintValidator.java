package com.yequan.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/10/12 10:12
 * @Description:
 */
public class PermissionTypeConstraintValidator implements ConstraintValidator<com.yequan.validation.annotation.PermissionTypeValidator, String> {

    private String types;

    @Override
    public void initialize(com.yequan.validation.annotation.PermissionTypeValidator constraintAnnotation) {
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
