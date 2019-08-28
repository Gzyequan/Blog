package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: yq
 * @Date: 2019/7/9 13:43
 * @Description:
 */
public interface FilterService {


    /**
     * 过滤处理
     *
     * @param data
     * @param filterRule
     */
    public void handle(JSONObject data, FilterRule filterRule);

}
