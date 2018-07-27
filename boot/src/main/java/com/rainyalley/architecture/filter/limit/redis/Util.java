package com.rainyalley.architecture.filter.limit.redis;

/**
 * @author bin.zhang
 */
public class Util {

    static String callerKey(String format, String caller){
        return format.replace("${caller}", caller);
    }

    static String targetKey(String format, String target){
        return format.replace("${target}", target);
    }

    static String callerTargetKey(String format, String caller, String target){
        return  format.replace("${caller}", caller).replace("${target}", target);
    }

    static String concat(String str1, String str2){
        return str1 + "|" + str2;
    }

    static String[] split(String text){
        return text.split("\\|");
    }
}
