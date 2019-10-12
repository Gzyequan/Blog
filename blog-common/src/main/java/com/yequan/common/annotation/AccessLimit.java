package com.yequan.common.annotation;

import java.lang.annotation.*;

/**
 * @Auther: yq
 * @Date: 2019/7/4 15:43
 * @Description: 接口限流注解
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    /**
     * 指定sec时间内访问的次数限制
     *
     * @return
     */
    int limit() default 5;

    /**
     * 时间长度
     * @return
     */
    int sec() default 5;

}
