package com.rainyalley.architecture.service.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
            this.logger.debug(String.format("%s(%s) = %s", method, Arrays.toString(args), result));
        }
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        String method = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("%s(%s)", method, Arrays.toString(args)), throwable);
        }
    }
}
