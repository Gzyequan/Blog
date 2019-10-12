package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/7/9 14:30
 * @Description: 过滤器环境
 */
public class FilterHandlerContext {

    /**
     * 过滤处理器池
     */
    private Map<String, FilterService> filterHandlerPool;

    public FilterHandlerContext() {
        filterHandlerPool = new HashMap<>();
    }

    /**
     * 根据过滤规则filterRule过滤数据
     *
     * @param data
     * @param filterRule
     */
    public void filterData(JSONObject data, FilterRule filterRule) {
        if (filterHandlerPool.size() > 0) {
            String filterType = filterRule.getType();
            if (StringUtils.isNotEmpty(filterType)) {
                FilterService filterService = filterHandlerPool.get(filterType);
                filterService.handle(data, filterRule);
            } else {
                System.out.println("过滤类型为空");
            }
        }
    }

    /**
     * 添加过滤处理器
     *
     * @param filterType
     * @param filterHandler
     */
    public void addFilterHandler(String filterType, FilterService filterHandler) {
        if (null != filterHandler)
            filterHandlerPool.put(filterType, filterHandler);
    }

}
