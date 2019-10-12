package com.yequan.common.util;

import com.yequan.constant.DateFormatConsts;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: yq
 * @Date: 2019/7/26 15:12
 * @Description: 时间工具类
 */
public class DateUtil {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取指定格式为 {format} 的当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentDateStr(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取格式为 yyyy-MM-dd HH:mm:ss 的当前时间
     *
     * @return
     */
    public static String getCurrentDateStr() {
        return getCurrentDateStr(DateFormatConsts.YYYY_MM_DD_HH_MM_SS);
    }


}
