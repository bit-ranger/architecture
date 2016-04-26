package com.rainyalley.architecture.common.aop;

import com.rainyalley.architecture.common.cache.CacheProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.Assert;

public class InvokeCacheAspect {

    private Log logger = LogFactory.getLog(getClass());

    private final static String separator = ":";

    private CacheProvider cacheProvider = new NullCacheProvider();

    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        PointContext context = new PointContext(joinPoint);
        String key = parseCacheKey(context);
        System.out.println(key);
        Class<?> returnType = context.getMethod().getReturnType();
        Object result =  doGet(key, returnType);
        if(result != null){
            if(logger.isDebugEnabled()){
                logger.debug(String.format("cache hit, key : <%s>", key));
            }
            return result;
        }
        if(logger.isDebugEnabled()){
            logger.debug(String.format("cache missing, key : <%s>", key));
        }
        result = joinPoint.proceed(joinPoint.getArgs());
        doPut(key, result);
        return result;
    }

    protected String parseCacheKey(PointContext context){
        String root = StringUtils.join(context.getTarget().getClass().getName().split("\\."), separator);
        String methodName = context.getMethod().getName();
        String paramText = "";
        for (Class<?> aClass : context.getMethod().getParameterTypes()) {
            paramText += "," + aClass.getName();
        }
        if(StringUtils.isNotBlank(paramText)){
            paramText = paramText.substring(1);
        }
        paramText = "(" + paramText + ")";

        String argText = "";
        for (Object object : context.getArgs()) {
            argText += "," + string2Unicode(object.toString());
        }
        if(StringUtils.isNotBlank(paramText)){
            argText = argText.substring(1);
        }
        argText = "(" + argText + ")";
        return root + separator + methodName + separator + paramText + separator + argText;
    }

    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    private void doPut(String key, Object value){
        cacheProvider.put(key, value);
        if(logger.isDebugEnabled()){
            logger.debug(String.format("Put value into cache, key : [%s]", key));
        }
    }

    private <V> V doGet(String key, Class<V> type){
        return cacheProvider.get(key, type);
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        Assert.notNull(cacheProvider, "cacheProvider can not be null");
        this.cacheProvider = cacheProvider;
    }

    private static class NullCacheProvider implements CacheProvider{

        @Override
        public void put(String key, Object value) {

        }

        @Override
        public <V> V get(String key, Class<V> type) {
            return null;
        }
    }
}
