package com.rainyalley.architecture.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private ThreadLocal<String> currentKeyHolder = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return currentKeyHolder.get();
    }

    public void setCurrentKey(String key){
        currentKeyHolder.set(key);
    }

    public String getCurrentKey(){
        return currentKeyHolder.get();
    }

    public void removeCurrentKey(){
        currentKeyHolder.remove();
    }
}
