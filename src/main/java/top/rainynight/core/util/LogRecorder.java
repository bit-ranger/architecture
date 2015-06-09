package top.rainynight.core.util;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class LogRecorder {

    public void record(JoinPoint joinPoint, Object returnValue){
        String loggerName = joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature();
        Logger logger = Logger.getLogger(loggerName);
        logger.debug(returnValue);
    }

}
