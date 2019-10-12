package com.yequan.common.util;

import com.yequan.constant.OSEnum;
import org.slf4j.LoggerFactory;

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

    public static void main(String[] args) {
        LoggerFactory.getLogger(OSUtil.class).debug("查看执行流程");
    }

}
