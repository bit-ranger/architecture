package com.rainyalley.architecture.batch.export;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Map;
import java.util.function.Function;

public class ItemFuntionFactoryBean<I, O> implements FactoryBean<Function<I, O>>, InitializingBean {

    private Map<String,Function<I, O>> exportSqlNameMappingItemFunction;

    private String targetName;

    @Override
    public Function<I, O> getObject() throws Exception {
        return exportSqlNameMappingItemFunction.get(targetName);
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }


    public void setExportSqlNameMappingItemFunction(Map<String, Function<I, O>> exportSqlNameMappingItemFunction) {
        this.exportSqlNameMappingItemFunction = exportSqlNameMappingItemFunction;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(exportSqlNameMappingItemFunction, "missing exportSqlNameMappingItemFunction");
        Assert.notNull(targetName, "missing targetName");
    }
}
