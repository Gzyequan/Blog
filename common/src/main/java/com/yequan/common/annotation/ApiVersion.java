package com.yequan.common.annotation;

import java.lang.annotation.*;

/**
 * @Auther: yq
 * @Date: 2019/9/6 11:28
 * @Description: API版本控制注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    //接口版本号
    int value() default 1;

}
