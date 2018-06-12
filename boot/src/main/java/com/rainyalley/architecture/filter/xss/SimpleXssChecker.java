package com.rainyalley.architecture.filter.xss;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class SimpleXssChecker implements XssChecker{

    private List<String> invalidString = Arrays.asList("<", ">", "%3C", "%3E");

    @Override
    public boolean isValid(String param) {
        for (String s : invalidString) {
            if(StringUtils.contains(param, s)){
                return false;
            }
        }
        return true;
    }
}
