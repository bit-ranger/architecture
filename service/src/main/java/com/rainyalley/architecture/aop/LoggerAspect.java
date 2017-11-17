package com.rainyalley.architecture.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

public class LoggerAspect {

    private final Log logger = LogFactory.getLog(this.getClass());

    public void afterReturning(JoinPoint joinPoint, Object result) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("Invoke complete, class : %s, method : %s, args : %s, return : %s", clazz, method, args, result));
        }
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();

        if (this.logger.isErrorEnabled()) {
            this.logger.error(String.format("Invoke interrupt, class : %s, method : %s, args : %s, throwable : %s", clazz, method, args, throwable));
        }
    }
}
