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
        filterServiceList.add(new SimpleFilterHandle());
        filterServiceList.add(new RegexFilterHandle());
        filterServiceList.add(new EncryptFilterHandle());
    }

    public void filterData(JSONObject data, FilterRule filterRule) {
        for (FilterService filterService : filterServiceList) {
            if (filterService.isMatch(filterRule)) {
                filterService.handle(data, filterRule);
                break;
            }
        }
    }

}
