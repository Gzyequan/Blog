package com.yequan.validation;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Auther: yq
 * @Date: 2019/9/4 10:58
 * @Description: 数据校验
 */
public class ValidationUtil {

    private static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

    /**
     * 校验对象
     *
     * @param t      bean
     * @param groups 校验组
     * @param <T>
     * @return
     */
    public static <T> ValidResult validateBean(T t, Class<T>... groups) {
        ValidResult result = new ValidationUtil().new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验bean中的一个属性
     *
     * @param obj          bean
     * @param propertyName 属性名称
     * @param <T>
     * @return
     */
    public static <T> ValidResult validateProperty(T obj, String propertyName) {
        ValidResult result = new ValidationUtil().new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validateProperty(obj, propertyName);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(propertyName, violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验结果对象
     */
    public class ValidResult {
        /**
         * 是否有错误
         */
        private boolean hasErrors;
        /**
         * 错误信息
         */
        private List<ErrorMessage> errors;

        public ValidResult() {
            this.errors = new ArrayList<>();
        }

        public boolean isHasErrors() {
            return hasErrors;
        }

        public void setHasErrors(boolean hasErrors) {
            this.hasErrors = hasErrors;
        }

        public List<ErrorMessage> getAllErrors() {
            return errors;
        }

        /**
         * 获取错误信息
         *
         * @return
         */
        public String getErrors() {
            StringBuilder errorMessages = new StringBuilder();
            for (ErrorMessage error : errors) {
                errorMessages.append(error.getPropertyPath()).append(":").append(error.getMessage()).append(" ");
            }
            return errorMessages.toString();
        }

        /**
         * 添加一个错误信息
         *
         * @param propertyPath
         * @param message
         */
        public void addError(String propertyPath, String message) {
            this.errors.add(new ErrorMessage(propertyPath, message));
        }
    }

    /**
     * 错误信息
     */
    public class ErrorMessage {

        private String propertyPath;
        private String message;

        public ErrorMessage() {
        }

        public ErrorMessage(String propertyPath, String message) {
            this.propertyPath = propertyPath;
            this.message = message;
        }

        public String getPropertyPath() {
            return propertyPath;
        }

        public void setPropertyPath(String propertyPath) {
            this.propertyPath = propertyPath;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
