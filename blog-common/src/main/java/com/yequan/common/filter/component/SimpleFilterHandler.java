package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: yq
 * @Date: 2019/7/9 13:56
 * @Description: 简单替换:对指定key的值直接替换成新值
 */
public class SimpleFilterHandler implements FilterService {

    @Override
    public void handle(JSONObject data, FilterRule filterRule) {
        if (null != data && null != filterRule) {
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
