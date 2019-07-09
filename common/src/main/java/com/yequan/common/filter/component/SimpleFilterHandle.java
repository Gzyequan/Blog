package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;
import com.yequan.common.application.AppConstant;

/**
 * @Auther: yq
 * @Date: 2019/7/9 13:56
 * @Description: 简单替换:对指定key的值直接替换成新值
 */
public class SimpleFilterHandle implements FilterService {
    @Override
    public boolean isMatch(FilterRule filterRule) {
        return AppConstant.FilterConstant.FILTER_SIMPLE.equals(filterRule.getType());
    }

    @Override
    public void handle(JSONObject data, FilterRule filterRule) {
        if (null != data) {
            String key = filterRule.getKey();
            if (data.containsKey(key)) {
                String newValue = filterRule.getReplacement();
                if (data.containsKey(key)) {
                    data.replace(key, newValue);
                }
            }
        }
    }
}
