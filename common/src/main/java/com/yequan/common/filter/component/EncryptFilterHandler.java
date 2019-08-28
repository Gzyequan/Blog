package com.yequan.common.filter.component;

import com.alibaba.fastjson.JSONObject;
import com.yequan.common.util.MD5Util;

/**
 * @Auther: yq
 * @Date: 2019/7/9 14:23
 * @Description: 对key的值进行加密, md5, 无盐值
 */
public class EncryptFilterHandler implements FilterService {

    @Override
    public void handle(JSONObject data, FilterRule filterRule) {
        if (null != data && null != filterRule) {
            String key = filterRule.getKey();
            if (data.containsKey(key)) {
                String oldValue = data.getString(key);
                String encryptValue = MD5Util.encrypt(oldValue);
                data.replace(key, encryptValue);
            }
        }
    }
}
