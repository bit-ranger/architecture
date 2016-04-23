package com.rainyalley.common;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class LogRecorder {

    public void record(JoinPoint joinPoint, Object returnValue){
        Logger logger = Logger.getLogger(joinPoint.getTarget().getClass());
        if(logger.isDebugEnabled()){
            String method = joinPoint.getSignature().toLongString();
            logger.debug(new StringBuilder("==> method : ").append(method).toString());
            logger.debug(new StringBuilder("==> return : ").append(returnValue).toString());
        }
    }

}
