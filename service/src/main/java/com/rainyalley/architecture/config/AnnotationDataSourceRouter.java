package com.rainyalley.architecture.config;

import com.rainyalley.architecture.aop.PointContext;
import com.rainyalley.architecture.aop.PointContextProvider;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

public class AnnotationDataSourceRouter extends AbstractRoutingDataSource {

    private static final String SLAVE_DB_KEY = "slave";
    private static final String MASTER_DB_KEY = "master";
    private PointContextProvider pointContextProvider;

    @Override
    protected Object determineCurrentLookupKey() {
        List<PointContext> joinPoints = this.pointContextProvider.trace();
        if (CollectionUtils.isEmpty(joinPoints)) {
            return AnnotationDataSourceRouter.SLAVE_DB_KEY;
        }

        PointContext first = joinPoints.get(0);
        Method method = first.getMethod();

        if (method != null && method.isAnnotationPresent(Transactional.class)) {
            Transactional annotation = method.getAnnotation(Transactional.class);

            if (annotation != null && !annotation.readOnly()) {
                return AnnotationDataSourceRouter.MASTER_DB_KEY;
            }
        }
        return AnnotationDataSourceRouter.SLAVE_DB_KEY;
    }

    public void setPointContextProvider(PointContextProvider pointContextProvider) {
        this.pointContextProvider = pointContextProvider;
    }
}
