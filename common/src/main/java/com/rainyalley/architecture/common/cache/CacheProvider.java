package com.rainyalley.architecture.common.cache;

public interface CacheProvider {

    void put(String key, Object value);

    <V> V get(String key, Class<V> type);

}
