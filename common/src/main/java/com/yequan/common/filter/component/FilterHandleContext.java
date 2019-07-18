package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/9 14:30
 * @Description:
 */
public class FilterHandleContext {

    private List<FilterService> filterServiceList;

    public FilterHandleContext() {
        filterServiceList = new ArrayList<>();
    }

    public void filterData(JSONObject data, FilterRule filterRule) {
        if (filterServiceList.size() > 0) {
            for (FilterService filterService : filterServiceList) {
                if (filterService.isMatch(filterRule)) {
                    filterService.handle(data, filterRule);
                    break;
                }
            }
        }
    }

    public void addFilter(FilterService filter) {
        if (null != filter)
            filterServiceList.add(filter);
    }

}
