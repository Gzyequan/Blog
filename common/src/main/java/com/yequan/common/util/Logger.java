package com.yequan.common.util;


import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerRepository;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.*;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 15:01
 * @Description: 自定义日志处理工具类
 */
public class Logger {

    //    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Logger.class);
    private static org.slf4j.Logger logger;
    private static final String FQCN = Logger.class.getName();

    static {
        try {
            /**
             * 使用cjlib动态代理技术代理org.apache.log4j.Logger对象,即使用自定义的Logger对象代理
             */
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(org.apache.log4j.Logger.class);
            enhancer.setCallbackType(LogInterceptor.class);
            //生成代理类
            Class proxyClass = enhancer.createClass();
            Enhancer.registerCallbacks(proxyClass, new LogInterceptor[]{new LogInterceptor()});

            //生成代理对象构造器
            Constructor<org.apache.log4j.Logger> constructor = proxyClass.getConstructor(String.class);
            //生成代理对象
            org.apache.log4j.Logger loggerProxy = constructor.newInstance(Logger.class.getName());

            /**
             * 使用jdk动态代理技术增强Log4jLoggerAdapter的logger成员变量,使其指向自定义生成的Logger对象loggerProxy
             */
            LoggerRepository loggerRepository = LogManager.getLoggerRepository();
            org.apache.log4j.spi.LoggerFactory log4jLoggerFactory
                    = ReflectionUtil.getFieldValue(loggerRepository, "defaultFactory");
            Object loggerFactoryProxy = Proxy.newProxyInstance(
                    org.apache.log4j.spi.LoggerFactory.class.getClassLoader(),
                    new Class[]{org.apache.log4j.spi.LoggerFactory.class},
                    new NewLoggerHandler(loggerProxy));
            //将LoggerRepository中成员变量defaultFactory的指向的对象换为代理对象loggerFactoryProxy
            ReflectionUtil.setFieldValue(loggerRepository, "defaultFactory", loggerFactoryProxy);
            //替换成自定义的Logger对象
            logger = org.slf4j.LoggerFactory.getLogger(Logger.class.getName());
            ReflectionUtil.setFieldValue(loggerRepository, "defaultFactory", log4jLoggerFactory);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * cjlib拦截器
     */
    private static class LogInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            //只拦截log方法
            if (args.length != 4 || !method.getName().equals("log"))
                return methodProxy.invokeSuper(o, args);
            // 替换传给log方法的第一个参数为我们自定义的FQCN
            args[0] = FQCN;
            return methodProxy.invokeSuper(o, args);
        }
    }

    /**
     * jdk动态代理方式
     */
    private static class NewLoggerHandler implements InvocationHandler {

        private final org.apache.log4j.Logger proxyLogger;

        public NewLoggerHandler(org.apache.log4j.Logger proxyLogger) {
            this.proxyLogger = proxyLogger;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return proxyLogger;
        }
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void error(String msg, Throwable throwable) {
        logger.error(msg, throwable);
    }

    public static void error(String format, Object... args) {
        logger.error(format, args);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void warn(String msg, Throwable throwable) {
        logger.warn(msg, throwable);
    }

    public static void warn(String format, Object... args) {
        logger.warn(format, args);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void info(String msg, Throwable throwable) {
        logger.info(msg, throwable);
    }

    public static void info(String format, Object... args) {
        logger.info(format, args);
    }

    public static void debug(String msg) {
        logger.debug(msg);
    }

    public static void debug(String msg, Throwable throwable) {
        logger.debug(msg, throwable);
    }

    public static void debug(String format, Object... args) {
        logger.debug(format, args);
    }

    public static void trace(String msg) {
        logger.trace(msg);
    }

    public static void trace(String msg, Throwable throwable) {
        logger.trace(msg, throwable);
    }

    public static void trace(String format, Object... args) {
        logger.trace(format, args);
    }


}
