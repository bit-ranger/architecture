package com.rainyalley.architecture.service.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
@Aspect
public class ServiceLoggerAspect {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Pointcut("execution(public * com.rainyalley.architecture.service..*.*(..))")
    public void pointcut(){ }


    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("After Returning, method : %s, args : %s, return : %s", method, args, result));
        }
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        String method = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();

        if (this.logger.isErrorEnabled()) {
            this.logger.error(String.format("After Throwing, method : %s, args : %s, throwable : %s", method, args, throwable));
        }
    }
}
