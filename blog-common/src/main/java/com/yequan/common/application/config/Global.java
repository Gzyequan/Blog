package com.yequan.common.application.config;

import com.google.common.collect.Maps;
import com.yequan.common.util.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/10/16 15:18
 * @Description: 全局配置类 懒汉式单例类.在第一次调用的时候实例化自己
 */
public class Global {

    private Global() {
    }

    /**
     * 当前对象实例
     */
    private static Global global = null;

    /**
     * 静态工厂方法 获取当前对象实例 多线程安全单例模式(使用双重同步锁)
     */

    public static synchronized Global getInstance() {

        if (global == null) {
            synchronized (Global.class) {
                if (global == null)
                    global = new Global();
            }
        }
        return global;
    }

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("application.properties");

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取普通用户角色id
     *
     * @return
     */
    public static Integer getCustomerRoleId() {
        String customerRoleId = getConfig("system.role.customer");
        return Integer.valueOf(customerRoleId);
    }

    /**
     * 获取超级管理员角色id
     *
     * @return
     */
    public static Integer getSuperAdminRoleId() {
        String customerRoleId = getConfig("system.role.superadmin");
        return Integer.valueOf(customerRoleId);
    }

}
