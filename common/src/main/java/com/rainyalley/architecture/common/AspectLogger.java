package com.rainyalley.architecture.common;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

/**
 * 切面日志工具
 */
public class AspectLogger {

    Log logger = LogFactory.getLog(getClass());

    public void record(JoinPoint joinPoint, Object returnValue){
        if(logger.isDebugEnabled()){
            Class<?> clazz = joinPoint.getTarget().getClass();
            String method = joinPoint.getSignature().toLongString();
            logger.debug(String.format("Invoke success, class : %s, method : %s, return : %s", clazz, method, returnValue));
        }
    }

}
