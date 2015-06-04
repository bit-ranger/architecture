package top.rainynight.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GenericUtils {
    private GenericUtils(){}

    public static Class<?> getActualClass(Class<?> clazz, int index){
        ParameterizedType pt = getParameterizedType(clazz);
        if(pt == null){
            String msg = String.format("%s is not a parameteried subclass",clazz.getTypeName());
            throw new IllegalArgumentException(msg);
        }

        Class<?> cla = getClass(pt, index);
        if(cla == null){
            String msg = String.format("parameterClass in %s at index %s not found", pt.getTypeName(), index);
            throw new IllegalArgumentException(msg);
        }
        return  cla;
    }

    /**
     * @param clazz 实例的类文字
     * @return 参数化的超类型
     */
    private static ParameterizedType getParameterizedType(Class<?> clazz){
        Type st = clazz.getGenericSuperclass();
        if(st == null){
            return null;
        }
        if(st instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType)st;
            return pt;
        } else{
            return getParameterizedType(clazz.getSuperclass());
        }
    }

    /**
     * @param pt 参数化超类型
     * @param index 类文字索引
     * @return 参数的类文字, 如果不为类文字, 则返回 null
     */
    private static Class<?> getClass(ParameterizedType pt, int index){
        Type param = pt.getActualTypeArguments()[index];
        if(param instanceof Class){
            return (Class<?>)param;
        } else {
            return null;
        }
    }
}
