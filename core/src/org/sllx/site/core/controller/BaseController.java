package org.sllx.site.core.controller;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
    private final static String SEARCH_PREFIX = "s_";

    protected Map<String, String> makeParam(ServletRequest request){
        Map<String, String> param = new HashMap<String, String>();
        Map<String, String[]> pm = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : pm.entrySet()) {
            String key = entry.getKey();
            int len = SEARCH_PREFIX.length();
            if(key.startsWith(SEARCH_PREFIX) && key.length() > len){
                key = key.substring(len);
                param.put(key, entry.getValue()[0]);
            }
        }
        return param;
    }


}
