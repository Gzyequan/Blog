package com.yequan.common.api;

import com.yequan.common.annotation.ApiVersion;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @Auther: yq
 * @Date: 2019/9/6 14:02
 * @Description: 识别@ApiVersion注解
 */
public class ApiHandlerMapping extends RequestMappingHandlerMapping {

    @Override   // 实例化类级别的@ApiVersion注解
    protected RequestCondition<ApiVersionRequestCondition> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(apiVersion);
    }

    @Override  // 实例化方法`级别的@ApiVersion注解
    protected RequestCondition<ApiVersionRequestCondition> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }

    // 实例化请求条件ApiVersionRequestCondition
    private RequestCondition<ApiVersionRequestCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionRequestCondition(apiVersion.value());
    }
}
