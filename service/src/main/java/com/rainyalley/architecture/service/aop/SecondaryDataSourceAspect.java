package com.rainyalley.architecture.service.aop;

import com.rainyalley.architecture.dao.util.RoutingDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Order(2)
@Component
@Aspect
public class SecondaryDataSourceAspect {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Resource
    private RoutingDataSource routingDataSource;

    @Around(value = "@annotation(secondary)")
    public void doBefore(ProceedingJoinPoint joinPoint, Secondary secondary) throws Throwable{

        String ck = routingDataSource.getCurrentKey();
        if(StringUtils.isNotBlank(ck)){
            return;
        }

        routingDataSource.setCurrentKey(secondary.value());
        if(logger.isDebugEnabled()){
            logger.debug("RoutingDataSource key set " + secondary.value());
        }

        try {
            joinPoint.proceed();
        } finally {
            routingDataSource.removeCurrentKey();
            if(logger.isDebugEnabled()){
                logger.debug("RoutingDataSource key remove " + secondary.value());
            }
        }
    }

}
