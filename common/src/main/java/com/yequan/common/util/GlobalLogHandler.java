package com.yequan.common.util;

import org.apache.log4j.Logger;

/**
 * @Auther: yq
 * @Date: 2019/8/28 16:48
 * @Description: log输出工具类
 */
public class GlobalLogHandler {

    private volatile static GlobalLogHandler instance = null;
    private static Logger logger = null;

    private GlobalLogHandler() {
    }

    public static GlobalLogHandler getInstance() {
        if (null == instance) {
            synchronized (GlobalLogHandler.class) {
                if (null == instance) {
                    instance = new GlobalLogHandler();
                }
            }
        }
        return instance;
    }

    public GlobalLogHandler setLoggerClass(Class clazz) {
        logger = Logger.getLogger(clazz);
        return this;
    }

    public void error(String msg) {
        logger.error(msgFormat(msg));
    }

    public void error(String msg, Throwable throwable) {
        logger.error(msgFormat(msg), throwable);
    }

    public void warn(String msg) {
        logger.warn(msgFormat(msg));
    }

    public void warn(String msg, Throwable throwable) {
        logger.warn(msgFormat(msg), throwable);
    }

    public void debug(String msg) {
        logger.debug(msgFormat(msg));
    }

    public void debug(String msg, Throwable throwable) {
        logger.debug(msgFormat(msg), throwable);
    }

    public void info(String msg) {
        logger.info(msgFormat(msg));
    }

    public void info(String msg, Throwable throwable) {
        logger.info(msgFormat(msg), throwable);
    }

    public void fatal(String msg) {
        logger.fatal(msgFormat(msg));
    }

    public void fatal(String msg, Throwable throwable) {
        logger.fatal(msgFormat(msg), throwable);
    }

    private String msgFormat(String msg) {
        return "------>>>" + msg + "<<<------";
    }

}
