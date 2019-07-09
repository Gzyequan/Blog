package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: yq
 * @Date: 2019/7/9 13:43
 * @Description:
 */
public interface FilterService {

    /**
     * 是否匹配正确的过滤方式
     *
     * @param filterRule
     * @return
     */
    public boolean isMatch(FilterRule filterRule);

    /**
     * 过滤处理
     *
     * @param data
     * @param filterRule
     */
    public void handle(JSONObject data, FilterRule filterRule);

}
