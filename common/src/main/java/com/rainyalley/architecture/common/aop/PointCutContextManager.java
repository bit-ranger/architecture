package com.rainyalley.architecture.common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayList;
import java.util.List;

public class PointCutContextManager {

    private Log logger = LogFactory.getLog(getClass());

    private static ThreadLocal<List<PointCutContext>> concurrentJoinPoints = new ThreadLocal<List<PointCutContext>>();


    public void before(JoinPoint joinPoint) throws Throwable{
        List<PointCutContext> joinPointList = concurrentJoinPoints.get();
        if(joinPointList == null){
            joinPointList = new ArrayList<PointCutContext>(1);
            concurrentJoinPoints.set(joinPointList);
        }
        joinPointList.add(new PointCutContext(joinPoint));
    }

    public void afterReturning(JoinPoint joinPoint, Object result){
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();

        if(logger.isDebugEnabled()){
            logger.debug(String.format("Invoke complete, class : %s, method : %s, args : %s, return : %s", clazz, method, args, result));
        }
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable throwable){
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();

        if(logger.isErrorEnabled()){
            logger.error(String.format("Invoke interrupt, class : %s, method : %s, args : %s, throwable : %s", clazz, method, args, throwable));
        }
    }

    public List<PointCutContext> trace(){
        return concurrentJoinPoints.get();
    }
}
