package org.sllx.mvc;

import org.sllx.core.util.Reflection;

import javax.servlet.ServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础控制层,包含一些控制层通用方法
 */
public abstract class Controller {
    private final static String VALID_PREFIX = "v_";

    protected final static String SUCCESS = "success";

    /**
     * 以{@link #VALID_PREFIX}开头的将被当做有效参数，此方法会将request中的有效参数收集起来以Map形式存储。
     * key将会去掉{@link #VALID_PREFIX}
     * @param request
     * @return 有效参数
     */
    protected Map<String, String> makeParam(ServletRequest request){
        Map<String, String> param = new HashMap<String, String>();
        Map<String, String[]> pm = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : pm.entrySet()) {
            String key = entry.getKey();
            int len = VALID_PREFIX.length();
            if(key.startsWith(VALID_PREFIX) && key.length() > len){
                key = key.substring(len);
                param.put(key, entry.getValue()[0]);
            }
        }
        return param;
    }

    /**
     * 将Map中的数据设置到Pojo中,Map中的key与Pojo中的set方法对应，需自行保证Map的正确性。
     * @param pojo 将被重置的bean
     * @param map 参数集合
     * @param <T>
     * @return 重置后的bean
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected <T> T reset(T pojo, Map<String,String> map) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String,String> entry : map.entrySet()) {
            Reflection.setProperty(pojo,entry.getKey(),entry.getValue());
        }
        return pojo;
    }
}
