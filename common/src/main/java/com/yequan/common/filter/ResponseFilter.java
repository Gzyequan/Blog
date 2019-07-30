package com.yequan.common.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yequan.common.application.constant.FilterConsts;
import com.yequan.common.filter.component.FilterHandleContext;
import com.yequan.common.filter.component.FilterRule;
import com.yequan.common.filter.component.RegexFilterHandle;
import com.yequan.common.filter.component.SimpleFilterHandle;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: yq
 * @Date: 2019/7/8 14:49
 * @Description: 请求返回值过滤器, 对一些特殊字段进行处理
 */
public class ResponseFilter extends BaseFilter implements Filter {

    private FilterHandleContext filterHandleContext;
    private FilterRule passwordFilter;
    private FilterRule mobilephoneFilter;

    public void init(FilterConfig filterConfig) throws ServletException {
        filterHandleContext = new FilterHandleContext();
        /**
         * 添加过滤处理器
         */
        filterHandleContext.addFilter(new SimpleFilterHandle());
        filterHandleContext.addFilter(new RegexFilterHandle());

        /**
         * 过滤password
         */
        passwordFilter = new FilterRule();
        passwordFilter.setKey("password");
        passwordFilter.setType(FilterConsts.FILTER_SIMPLE);
        passwordFilter.setReplacement("");

        /**
         * 过滤mobilephone
         */
        mobilephoneFilter = new FilterRule();
        mobilephoneFilter.setKey("mobilephone");
        mobilephoneFilter.setType(FilterConsts.FILTER_REGEX);
        mobilephoneFilter.setRegex("(\\d{3})\\d{4}(\\d{4})");
        mobilephoneFilter.setReplacement("$1****$2");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        //只拦截返回值,请求放行
        filterChain.doFilter(servletRequest, responseWrapper);
        //过滤返回值
        responseFilterHandle(servletResponse, responseWrapper);
    }

    /**
     * 过滤返回值中的特殊字段
     *
     * @param servletResponse
     * @param responseWrapper
     * @throws IOException
     */
    private void responseFilterHandle(ServletResponse servletResponse, ResponseWrapper responseWrapper)
            throws IOException {
        byte[] content = responseWrapper.getContent();
        String responseText = new String(content, StandardCharsets.UTF_8);
        if (!responseText.equalsIgnoreCase("")) {
            JSONObject responseObject = JSON.parseObject(responseText);
            Object data = responseObject.get("data");
            if (null != data) {
                if (data instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) data;
                    filterHandleContext.filterData(jsonObject, passwordFilter);
                    filterHandleContext.filterData(jsonObject, mobilephoneFilter);
                } else if (data instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) data;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        filterHandleContext.filterData(jsonObject, passwordFilter);
                        filterHandleContext.filterData(jsonObject, mobilephoneFilter);
                    }
                }
            }
            responseText = JSON.toJSONString(responseObject);
        }
        write(servletResponse, responseText);
    }

    public void destroy() {

    }
}
