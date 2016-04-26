package com.rainyalley.architecture.common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayList;
import java.util.List;

public class PointContextCollectAspect implements PointContextProvider{

    private Log logger = LogFactory.getLog(getClass());

    private static ThreadLocal<List<PointContext>> concurrentJoinPoints = new ThreadLocal<List<PointContext>>();

    public void weave(JoinPoint joinPoint) throws Throwable{
        List<PointContext> joinPointList = concurrentJoinPoints.get();
        if(joinPointList == null){
            joinPointList = new ArrayList<PointContext>(1);
            concurrentJoinPoints.set(joinPointList);
            if(logger.isDebugEnabled()){
                logger.debug(String.format("Initialize a new PointCutContext stack for thread : %s", Thread.currentThread().getName()));
            }
        }
        joinPointList.add(new PointContext(joinPoint));
        if(logger.isDebugEnabled()){
            logger.debug(String.format("Add a new PointCutContext for thread : %s", Thread.currentThread().getName()));
        }
    }

    public List<PointContext> trace(){
        return concurrentJoinPoints.get();
    }
}
