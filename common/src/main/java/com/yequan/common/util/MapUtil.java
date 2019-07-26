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
        Map<String, Object> map = new HashMap<>();
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            if (fields.length > 0) {
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
            e.printStackTrace();
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
        Object targetObject = null;
        try {
            if (null == map || map.isEmpty()) {
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
                Field field = getClassField(clazz, propertyName);
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
            e.printStackTrace();
        }
        return (T) targetObject;
    }

    /**
     * 根据fieldName获取clazz对象中的field
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        //排除Object
        if (Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        //获取所有的filed
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //根据fieldName匹配对应的field
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        //如果在没有取到,则判断是否存在父类,如果存在父类也将父类中的field也取出
        Class<?> superclass = clazz.getSuperclass();
        if (null != superclass) {
            //递归获取
            return getClassField(superclass, fieldName);
        }
        return null;
    }

    /**
     * 根据属性类型将属性值转换成正确的数据类型
     *
     * @param propertyValue
     * @param fieldTypeClass
     * @return
     */
    private static Object convertValueType(Object propertyValue, Class<?> fieldTypeClass) {
        Object targetValue = null;
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
        return targetValue;
    }

}
