package com.yequan.common.annotation;

import java.lang.annotation.*;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 8:45
 * @Description: 接口幂等性
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
}
