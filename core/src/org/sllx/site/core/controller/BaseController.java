package org.sllx.site.core.controller;

import org.sllx.site.core.support.Reflection;

import javax.servlet.ServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
    private final static String VALID_PREFIX = "v_";

    protected final static String SUCCESS = "success";

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

    protected <T> T reset(T pojo, Map<String,String> map) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String,String> entry : map.entrySet()) {
            Reflection.setProperty(pojo,entry.getKey(),entry.getValue());
        }
        return pojo;
    }
}
