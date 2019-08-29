package com.yequan.common.util;

import org.apache.log4j.Logger;

/**
 * @Auther: yq
 * @Date: 2019/8/28 16:48
 * @Description: log输出工具类
 */
public class LogUtil {

    private static Logger logger = Logger.getLogger(LogUtil.class);

    public static void error(String msg) {
        logger.error(msgFormat(msg));
    }

    public static void error(String msg, Throwable throwable) {
        logger.error(msgFormat(msg), throwable);
    }

    public static void warn(String msg) {
        logger.warn(msgFormat(msg));
    }

    public static void warn(String msg, Throwable throwable) {
        logger.warn(msgFormat(msg), throwable);
    }

    public static void debug(String msg) {
        logger.debug(msgFormat(msg));
    }

    public static void debug(String msg, Throwable throwable) {
        logger.debug(msgFormat(msg), throwable);
    }

    public static void info(String msg) {
        logger.info(msgFormat(msg));
    }

    public static void info(String msg, Throwable throwable) {
        logger.info(msgFormat(msg), throwable);
    }

    public static void fatal(String msg) {
        logger.fatal(msgFormat(msg));
    }

    public static void fatal(String msg, Throwable throwable) {
        logger.fatal(msgFormat(msg), throwable);
    }

    private static String msgFormat(String msg) {
        return "---------" + msg + "---------";
    }

}
