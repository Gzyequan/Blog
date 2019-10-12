package com.yequan.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/7/25 16:52
 * @Description:
 */
public class MapUtil {

    /**
     * 对象转换成map
     *
     * @param object
     * @return
     */
    public static Map<String, Object> objectToMap(Object object) {
        Logger.debug(" objectToMap object :{}", object);
        Map<String, Object> map = null;
        try {
            if (null == object) {
                return null;
            }
            Field[] fields = object.getClass().getDeclaredFields();
            if (fields.length > 0) {
                map = new HashMap<>();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    Object fieldValue = field.get(object);
                    map.put(fieldName, fieldValue);
                    field.setAccessible(accessible);
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * map集合转对象
     * 思路
     * 1.遍历map集合,获取键和值
     * 2.用反射通过setXXX方法将map中的值设置进对象中对应的属性中
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<?> clazz) {
        Logger.debug("mapToObject");
        Object targetObject = null;
        try {
            if ((null == map || map.size() < 1) || null == clazz) {
                return null;
            }
            targetObject = clazz.newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                //属性名
                String propertyName = entry.getKey();
                //属性值
                Object propertyValue = entry.getValue();
                //对象中的set方法
                String setMethod = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                //根据属性名获取对应的field,通过field能确定属性的类型
                Field field = ReflectionUtil.getClassField(clazz, propertyName, true);
                if (null == field) {
                    continue;
                }
                //根据属性类型将值进行正确的类型转换
                Class<?> fieldType = field.getType();
                propertyValue = convertValueType(propertyValue, fieldType);
                //核心方法,根据setXXX方法设置值
                clazz.getMethod(setMethod, fieldType).invoke(targetObject, propertyValue);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return (T) targetObject;
    }

    /**
     * 根据属性类型将属性值转换成正确的数据类型
     *
     * @param propertyValue
     * @param fieldTypeClass
     * @return
     */
    private static Object convertValueType(Object propertyValue, Class<?> fieldTypeClass) {
        if (null == propertyValue || null == fieldTypeClass) {
            return null;
        }
        Object targetValue = null;
        try {
            if (Long.class.getName().equals(fieldTypeClass.getName())
                    || long.class.getName().equals(fieldTypeClass.getName())) {
                targetValue = Long.parseLong(propertyValue.toString());
            } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                    || int.class.getName().equals(fieldTypeClass.getName())) {
                targetValue = Integer.parseInt(propertyValue.toString());
            } else if (Float.class.getName().equals(fieldTypeClass.getName())
                    || float.class.getName().equals(fieldTypeClass.getName())) {
                targetValue = Float.parseFloat(propertyValue.toString());
            } else if (Double.class.getName().equals(fieldTypeClass.getName())
                    || double.class.getName().equals(fieldTypeClass.getName())) {
                targetValue = Double.parseDouble(propertyValue.toString());
            } else {
                targetValue = propertyValue;
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return targetValue;
    }

}
