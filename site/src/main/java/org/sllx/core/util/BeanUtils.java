package org.sllx.core.util;

import org.sllx.core.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

    public static Method getMethod(Object object, String methodName){
        Assert.notNull(object);
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }


    public static Object setProperty(Object bean, String field, Object value) throws InvocationTargetException, IllegalAccessException {
        String methodName = toSetterName(field);
        Method method = getMethod(bean,methodName);
        if(method == null || method.getParameterTypes().length != 1){
            return false;
        }
        Class<?> toType = method.getParameterTypes()[0];
        value = TypeConverter.getInstance().convert(value,toType);
        return method.invoke(bean, value);
    }

    private static String toSetterName(String field){
        if(field.length() >= 2 && Character.isUpperCase(field.charAt(1))){
            return "set" + field;
        }
        return "set" + field.substring(0,1).toUpperCase() + field.substring(1);
    }
}
