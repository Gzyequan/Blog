package com.yequan.common.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yequan.common.application.constant.RegexConsts;
import com.yequan.common.filter.component.FilterHandlerContext;
import com.yequan.common.filter.component.FilterRule;
import com.yequan.common.filter.component.RegexFilterHandler;
import com.yequan.common.filter.component.SimpleFilterHandler;

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

    private FilterHandlerContext filterHandlerContext;
    private FilterRule passwordFilter;
    private FilterRule mobilephoneFilter;
    private final String[] filterType = {"simple", "regex", "encrypt"};

    public void init(FilterConfig filterConfig) throws ServletException {
        filterHandlerContext = new FilterHandlerContext();
        /**
         * 添加过滤处理器
         */
        filterHandlerContext.addFilterHandler(filterType[0], new SimpleFilterHandler());
        filterHandlerContext.addFilterHandler(filterType[1], new RegexFilterHandler());

        /**
         * password过滤规则:SimpleFilterHandler
         */
        passwordFilter = new FilterRule();
        passwordFilter.setKey("password");
        passwordFilter.setType(filterType[0]);
        passwordFilter.setReplacement("");

        /**
         * mobilephone过滤规则:RegexFilterHandler
         */
        mobilephoneFilter = new FilterRule();
        mobilephoneFilter.setKey("mobilephone");
        mobilephoneFilter.setType(filterType[1]);
        mobilephoneFilter.setRegex(RegexConsts.REGEX_MOBILE_CENTER_4);
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
                    filterHandlerContext.filterData(jsonObject, passwordFilter);
                    filterHandlerContext.filterData(jsonObject, mobilephoneFilter);
                } else if (data instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) data;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        filterHandlerContext.filterData(jsonObject, passwordFilter);
                        filterHandlerContext.filterData(jsonObject, mobilephoneFilter);
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
