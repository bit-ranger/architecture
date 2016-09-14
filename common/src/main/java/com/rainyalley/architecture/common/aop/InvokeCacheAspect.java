package com.rainyalley.architecture.common.aop;

import com.rainyalley.architecture.common.cache.CacheProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.Assert;

import java.util.MissingResourceException;

public class InvokeCacheAspect {

    private static final String separator = ":";
    private final Log logger = LogFactory.getLog(this.getClass());
    private CacheProvider cacheProvider = new InvokeCacheAspect.NullCacheProvider();

    public static String string2Unicode(String string) {

        StringBuilder unicode = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        PointContext context = new PointContext(joinPoint);
        String key = this.parseCacheKey(context);
        Class<?> returnType = context.getMethod().getReturnType();

        Object result = null;
        try {
            result = this.doGet(key, returnType);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(String.format("Cache hit, key : <%s>", key));
            }
            return result;
        } catch (MissingResourceException e) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(String.format("Cache missing, key : <%s>", key));
            }
        }

        result = joinPoint.proceed(joinPoint.getArgs());
        this.doPut(key, result);
        return result;
    }

    protected String parseCacheKey(PointContext context) {
        String root = StringUtils.join(context.getTarget().getClass().getName().split("\\."), InvokeCacheAspect.separator);
        String methodName = context.getMethod().getName();
        String paramText = "";
        for (Class<?> aClass : context.getMethod().getParameterTypes()) {
            paramText += "," + aClass.getName();
        }
        if (StringUtils.isNotBlank(paramText)) {
            paramText = paramText.substring(1);
        }
        paramText = "(" + paramText + ")";

        String argText = "";
        for (Object object : context.getArgs()) {
            argText += "," + InvokeCacheAspect.string2Unicode(object.toString());
        }
        if (StringUtils.isNotBlank(paramText)) {
            argText = argText.substring(1);
        }
        argText = "(" + argText + ")";
        return root + InvokeCacheAspect.separator + methodName + InvokeCacheAspect.separator + paramText + InvokeCacheAspect.separator + argText;
    }

    private void doPut(String key, Object value) {
        this.cacheProvider.put(key, value);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("Put value into cache, key : <%s>", key));
        }
    }

    private <V> V doGet(String key, Class<V> type) throws MissingResourceException {
        return this.cacheProvider.get(key, type);
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        Assert.notNull(cacheProvider, "cacheProvider can not be null");
        this.cacheProvider = cacheProvider;
    }

    private static class NullCacheProvider implements CacheProvider {

        @Override
        public boolean put(String key, Object value) {
            return true;
        }

        @Override
        public <V> V get(String key, Class<V> type) {
            return null;
        }
    }
}
