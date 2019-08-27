package com.yequan.common.annotation;


import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CrossPermission {

    /**
     * 参数关键字
     */
    String key();

    /**
     * 是否是路径传参:使用@PathVariable注解的参数
     */
    boolean isPathVariable() default true;
}
