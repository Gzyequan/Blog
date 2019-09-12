package com.yequan.common.api;

import com.yequan.common.util.Logger;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: yq
 * @Date: 2019/9/6 13:39
 * @Description: 自定义请求匹配规则
 */
public class ApiVersionRequestCondition implements RequestCondition<ApiVersionRequestCondition> {

    // 路径中版本号的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");

    private int apiVersion;

    public ApiVersionRequestCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * 将不同的筛选条件合并,这里采用的覆盖，即后来的规则生效
     *
     * @param other
     * @return
     */
    public ApiVersionRequestCondition combine(ApiVersionRequestCondition other) {
        return new ApiVersionRequestCondition(other.getApiVersion());
    }

    /**
     * 根据request查找匹配到的筛选条件
     *
     * @param request
     * @return
     */
    public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
        Logger.debug(request.getRequestURI());
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if (m.find()) {
            Integer version = Integer.valueOf(m.group(1));
            if (version >= this.apiVersion) // 如果请求的版本号大于配置版本号， 则满足，即与请求的
                return this;
        }
        return null;
    }

    /**
     * 实现不同条件类的比较，从而实现优先级排序
     *
     * @param other
     * @param request
     * @return
     */
    public int compareTo(ApiVersionRequestCondition other, HttpServletRequest request) {
        return other.getApiVersion() - this.apiVersion;
    }

    public int getApiVersion() {
        return apiVersion;
    }

}
