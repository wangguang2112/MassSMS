package com.wang.masssms.uiview.util;

import java.lang.reflect.Field;

/**
 * 帮帮项目组 Bangbang
 *
 * @Author zhaobo
 * @Date 2015/5/23 10:16
 * @Copyright:58.com Inc. All rights reserved.
 */
public class ReflectionUtils {
    public static Object getFiled(Object object, String fieldName) throws Exception {
        Field field = getDeclaredField(object, fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    private static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        Class clazz = (Class) object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {

            }
        }
        return null;
    }
}
