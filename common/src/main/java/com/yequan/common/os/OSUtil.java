package com.yequan.common.os;

/**
 * @Auther: yq
 * @Date: 2019/7/2 11:27
 * @Description:
 */
public class OSUtil {

    public static int OS_OTHER = -1;

    public static int OS_WIN = 0;

    public static int OS_LINUX = 1;

    /**
     * 获取当前操作系统信息
     *
     * @return
     */
    public static int getOSInfo() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.startsWith("win")) {
            return OS_WIN;
        } else if (OS.startsWith("linux")) {
            return OS_LINUX;
        }
        return OS_OTHER;
    }

}
