package com.yequan.common.api;

import com.yequan.common.annotation.ApiVersion;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @Auther: yq
 * @Date: 2019/9/6 14:02
 * @Description: 设置自定义请求条件
 */
public class ApiHandlerMapping extends RequestMappingHandlerMapping {

    //最近一次的版本号
    private int latestVersion = 1;

    /**
     * 获取类中自定义请求RequestCondition
     *
     * @param handlerType
     * @return
     */
    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        //判断类中是否有@ApiVersion注解,构建基于@ApiVersion注解的RequestCondition
        ApiVersionRequestCondition condition = buildFrom(AnnotationUtils.findAnnotation(handlerType, ApiVersion.class));
        //保存最大版本号
        if (null != condition && condition.getVersion() > latestVersion) {
            ApiVersionRequestCondition.setMaxVersion(condition.getVersion());
        }
        return condition;
    }

    /**
     * 获取方法中自定义请求RequestCondition
     *
     * @param method
     * @return
     */
    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        //判断方法中是否有@ApiVersion注解,构建基于@ApiVersion注解的RequestCondition
        ApiVersionRequestCondition condition = buildFrom(AnnotationUtils.findAnnotation(method, ApiVersion.class));
        //保存最大版本号
        if (null != condition && condition.getVersion() > latestVersion) {
            ApiVersionRequestCondition.setMaxVersion(condition.getVersion());
        }
        return condition;
    }

    /**
     * 构建ApiVersion
     *
     * @param apiVersion
     * @return
     */
    private ApiVersionRequestCondition buildFrom(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionRequestCondition(apiVersion.value());
    }
}
