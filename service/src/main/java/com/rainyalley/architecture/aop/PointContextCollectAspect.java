package com.rainyalley.architecture.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.ArrayList;
import java.util.List;

//@Component
//@Aspect
public class PointContextCollectAspect implements PointContextProvider {

    private static final ThreadLocal<List<PointContext>> concurrentJoinPoints = new ThreadLocal<List<PointContext>>();
    private final Log logger = LogFactory.getLog(this.getClass());


    @Pointcut("execution(public * com.rainyalley.architecture.impl.ServiceBasicSupport.*(..)) || execution(public * com.rainyalley.architecture.service..service..*.*(..))")
    public void pointcut(){ }


    @Before("pointcut()")
    public void weave(JoinPoint joinPoint) throws Throwable {
        List<PointContext> joinPointList = PointContextCollectAspect.concurrentJoinPoints.get();
        if (joinPointList == null) {
            joinPointList = new ArrayList<PointContext>(1);
            PointContextCollectAspect.concurrentJoinPoints.set(joinPointList);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(String.format("Initialize a new PointCutContext stack for thread : %s", Thread.currentThread().getName()));
            }
        }
        joinPointList.add(new PointContext(joinPoint));
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("Add a new PointCutContext for thread : %s", Thread.currentThread().getName()));
        }
    }

    @Override
    public List<PointContext> trace() {
        return PointContextCollectAspect.concurrentJoinPoints.get();
    }
}
