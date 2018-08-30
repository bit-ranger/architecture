package com.rainyalley.architecture.batch.export;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Map;

public class DataSourceFactoryBean implements FactoryBean<DataSource>, InitializingBean {

    private Map<String,DataSource> nameMappingDataSource;

    private String targetName;

    @Override
    public DataSource getObject() throws Exception {
        return nameMappingDataSource.get(targetName);
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    public void setNameMappingDataSource(Map<String, DataSource> nameMappingDataSource) {
        this.nameMappingDataSource = nameMappingDataSource;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(nameMappingDataSource, "missing nameMappingDataSource");
        Assert.notNull(targetName, "missing targetName");
    }
}
