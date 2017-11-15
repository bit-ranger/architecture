package com.rainyalley.architecture.service.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayList;
import java.util.List;

public class PointContextCollectAspect implements PointContextProvider {

    private static final ThreadLocal<List<PointContext>> concurrentJoinPoints = new ThreadLocal<List<PointContext>>();
    private final Log logger = LogFactory.getLog(this.getClass());

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
