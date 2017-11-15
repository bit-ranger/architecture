package com.rainyalley.architecture.common.cache;

import java.util.MissingResourceException;

public interface CacheProvider {

    boolean put(String key, Object value);

    <V> V get(String key, Class<V> type) throws MissingResourceException;
}
