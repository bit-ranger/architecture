package org.sllx.site.core.support;

import java.lang.reflect.Field;

/**
 * Created by sllx on 14-10-14.
 */
public class Reflection {

    public static Field[] getFields(Object object){
        Assert.notNull(object);
        return object.getClass().getDeclaredFields();
    }

    public static Object getValue(Field field, Object host){
        field.setAccessible(true);
        try {
            return field.get(host);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }
}
