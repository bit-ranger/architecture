package com.rainyalley.architecture.common.datasource;

import com.rainyalley.architecture.common.aop.PointCutContext;
import com.rainyalley.architecture.common.aop.PointCutContextManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

public class KeywordDataSourceRouter extends AbstractRoutingDataSource {

    private PointCutContextManager pointCutContextManager;

    private final static String READ_DB_KEY = "slave";

    private final static String WRITE_DB_KEY = "master";

    @Override
    protected Object determineCurrentLookupKey() {
        List<PointCutContext> joinPoints = pointCutContextManager.trace();
        if(CollectionUtils.isEmpty(joinPoints)){
            return READ_DB_KEY;
        }

        PointCutContext first = joinPoints.get(0);
        Method method = first.getMethod();

        if (method != null && method.isAnnotationPresent(Transactional.class)){
            Transactional annotation = method.getAnnotation(Transactional.class);

            if(annotation != null && !annotation.readOnly()){
                return WRITE_DB_KEY;
            }
        }
        return READ_DB_KEY;
    }

    public void setPointCutContextManager(PointCutContextManager pointCutContextManager) {
        this.pointCutContextManager = pointCutContextManager;
    }
}
