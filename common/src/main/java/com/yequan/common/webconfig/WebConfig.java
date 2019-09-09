package com.yequan.common.webconfig;

import com.yequan.common.api.ApiHandlerMapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @Auther: yq
 * @Date: 2019/9/6 15:08
 * @Description: SpringMvc全局配置
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new ApiHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        return handlerMapping;
    }


}
