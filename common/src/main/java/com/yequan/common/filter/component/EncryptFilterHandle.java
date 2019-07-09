package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;
import com.yequan.common.application.AppConstant;
import com.yequan.common.util.MD5Util;

/**
 * @Auther: yq
 * @Date: 2019/7/9 14:23
 * @Description: 对key的值进行加密, md5, 无盐值
 */
public class EncryptFilterHandle implements FilterService {
    @Override
    public boolean isMatch(FilterRule filterRule) {
        return AppConstant.FilterConstant.FILTER_ENCRYPT.equals(filterRule.getType());
    }

    @Override
    public void handle(JSONObject data, FilterRule filterRule) {
        if (null != data) {
            String key = filterRule.getKey();
            if (data.containsKey(key)) {
                String oldValue = data.getString(key);
                String encryptValue = MD5Util.encrypt(oldValue);
                data.replace(key, encryptValue);
            }
        }
    }
}
