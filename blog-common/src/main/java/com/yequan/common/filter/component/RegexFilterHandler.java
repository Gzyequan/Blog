package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: yq
 * @Date: 2019/7/9 14:08
 * @Description: 只适用于正则替换
 */
public class RegexFilterHandler implements FilterService {

    @Override
    public void handle(JSONObject data, FilterRule filterRule) {
        if (null != data && null != filterRule) {
            String key = filterRule.getKey();
            if (data.containsKey(key)) {
                String regex = filterRule.getRegex();
                String replacement = filterRule.getReplacement();
                String oldValue = data.getString(key);
                String newValue = oldValue.replaceAll(regex, replacement);
                data.replace(key, newValue);
            }
        }
    }
}
