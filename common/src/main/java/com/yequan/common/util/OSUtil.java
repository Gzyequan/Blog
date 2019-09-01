package com.yequan.common.util;

import com.yequan.common.application.constant.OSEnum;

/**
 * @Auther: yq
 * @Date: 2019/7/2 11:27
 * @Description: 获取操作系统信息工具类
 */
public class OSUtil {

    /**
     * 获取当前操作系统信息
     *
     * @return
     */
    public static int getOSInfo() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.startsWith("win")) {
            return OSEnum.OS_WIN.getCode();
        } else if (OS.startsWith("linux")) {
            return OSEnum.OS_LINUX.getCode();
        }
        return OSEnum.OTHER.getCode();
    }

}
