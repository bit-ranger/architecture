package com.rainyalley.architecture.service.datasource;

import com.rainyalley.architecture.service.aop.PointContext;
import com.rainyalley.architecture.service.aop.PointContextProvider;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

public class KeywordDataSourceRouter extends AbstractRoutingDataSource {

    private static final String READ_DB_KEY = "slave";
    private static final String WRITE_DB_KEY = "master";
    private PointContextProvider pointContextProvider;

    @Override
    protected Object determineCurrentLookupKey() {
        List<PointContext> joinPoints = this.pointContextProvider.trace();
        if (CollectionUtils.isEmpty(joinPoints)) {
            return KeywordDataSourceRouter.READ_DB_KEY;
        }

        PointContext first = joinPoints.get(0);
        Method method = first.getMethod();

        if (method != null && method.isAnnotationPresent(Transactional.class)) {
            Transactional annotation = method.getAnnotation(Transactional.class);

            if (annotation != null && !annotation.readOnly()) {
                return KeywordDataSourceRouter.WRITE_DB_KEY;
            }
        }
        return KeywordDataSourceRouter.READ_DB_KEY;
    }

    public void setPointContextProvider(PointContextProvider pointContextProvider) {
        this.pointContextProvider = pointContextProvider;
    }
}
