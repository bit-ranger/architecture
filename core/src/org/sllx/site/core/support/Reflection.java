package org.sllx.site.core.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sllx on 14-10-14.
 */
public class Reflection {

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


    public static boolean setProperty(Object bean, String name, Object value) throws InvocationTargetException, IllegalAccessException {
        String methodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);
        Method method = getMethod(bean,methodName);
        if(method == null || method.getParameterCount() != 1){
            return false;
        }
        Class<?> toType = method.getParameterTypes()[0];
        value = TypeConverter.getInstance().convert(value,toType);
        method.invoke(bean, value);
        return true;
    }
}
