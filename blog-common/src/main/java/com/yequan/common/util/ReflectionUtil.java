package com.yequan.common.util;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 15:29
 * @Description: 反射工具类
 */
public class ReflectionUtil {

    public static <T> T getFieldValue(@NotNull Object object,
                                      @NotNull String fieldName) throws IllegalAccessException {
        return getFieldValue(object, fieldName, false);
    }

    private static <T> T getFieldValue(@NotNull Object object,
                                       @NotNull String fieldName,
                                       boolean traceable) throws IllegalAccessException {
        String[] fieldNames = fieldName.split("\\.");
        for (String targetField : fieldNames) {
            Field field = getClassField(object.getClass(), targetField, traceable);
            if (null == field) {
                return null;
            }
            object = getValue(object, field);
        }
        return null;
    }

    /**
     * 在一个class中查找名为targetClass的Field
     *
     * @param clazz       查找的类
     * @param targetField 查找的名
     * @param traceable   是否往父类查找
     * @return
     */
    public static Field getClassField(Class clazz, String targetField, boolean traceable) {
        if (null == clazz || null == targetField) {
            return null;
        }
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(targetField)) {
                    return field;
                }
            }
            //往父类中找名为targetFiled的Field
            clazz = clazz.getSuperclass();
            //终止条件,不往父类寻找或者找到的父类为Object时终止
            traceable = traceable && clazz != Object.class;
        }
        while (traceable);
        return null;
    }

    private static <T> T getValue(Object target, Field field) throws IllegalAccessException {
        if (!field.isAccessible())
            field.setAccessible(true);
        return (T) field.get(target);
    }

    public static boolean setFieldValue(@NotNull Object target,
                                        @NotNull String fieldName,
                                        @NotNull Object value) throws IllegalAccessException {
        return setFieldValue(target, fieldName, value, false);
    }

    public static boolean setFieldValue(@NotNull Object target,
                                        @NotNull String fieldName,
                                        @NotNull Object value,
                                        boolean traceable) throws IllegalAccessException {
        Field field = getClassField(target.getClass(), fieldName, traceable);
        if (field != null)
            return setValue(field, target, value);
        return false;
    }

    private static boolean setValue(Field field, Object target, Object value) throws IllegalAccessException {
        if (!field.isAccessible())
            field.setAccessible(true);
        field.set(target, value);
        return true;
    }

}
