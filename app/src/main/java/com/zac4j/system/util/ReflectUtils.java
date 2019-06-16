package com.zac4j.system.util;

import java.lang.reflect.Field;

/**
 * Reflect utility methods
 *
 * @author: zac
 * @date: 2019-06-16
 */
public class ReflectUtils {

    public static Object getFieldValue(String fieldName, Object target) {
        try {
            return getFieldValueUnchecked(fieldName, target);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object getFieldValueUnchecked(String fieldName, Object target)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = findField(fieldName, target.getClass());

        field.setAccessible(true);
        return field.get(target);
    }

    static Field findField(String name, Class clazz) throws NoSuchFieldException {
        Class currentClass = clazz;
        while (currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (name.equals(field.getName())) {
                    return field;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        throw new NoSuchFieldException("Field " + name + " not found for class " + clazz);
    }
}
