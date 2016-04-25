package com.rainyalley.architecture.common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayList;
import java.util.List;

public class PointCutContextManager {

    private Log logger = LogFactory.getLog(getClass());

    private static ThreadLocal<List<PointCutContext>> concurrentJoinPoints = new ThreadLocal<List<PointCutContext>>();

    public void weave(JoinPoint joinPoint) throws Throwable{
        List<PointCutContext> joinPointList = concurrentJoinPoints.get();
        if(joinPointList == null){
            joinPointList = new ArrayList<PointCutContext>(1);
            concurrentJoinPoints.set(joinPointList);
            if(logger.isDebugEnabled()){
                logger.debug(String.format("initialize a new PointCutContext stack for thread : %s", Thread.currentThread().getName()));
            }
        }
        joinPointList.add(new PointCutContext(joinPoint));
        if(logger.isDebugEnabled()){
            logger.debug(String.format("add a new PointCutContext for thread : %s", Thread.currentThread().getName()));
        }
    }

    public List<PointCutContext> trace(){
        return concurrentJoinPoints.get();
    }
}
