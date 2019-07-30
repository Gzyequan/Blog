package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;
import com.yequan.common.application.constant.FilterConsts;

/**
 * @Auther: yq
 * @Date: 2019/7/9 14:08
 * @Description: 只适用于正则替换
 */
public class RegexFilterHandle implements FilterService {

    @Override
    public boolean isMatch(FilterRule filterRule) {
        return FilterConsts.FILTER_REGEX.equals(filterRule.getType());
    }

    @Override
    public void handle(JSONObject data, FilterRule filterRule) {
        if (null != data) {
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
