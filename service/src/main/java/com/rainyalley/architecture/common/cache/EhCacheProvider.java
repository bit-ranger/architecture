package com.rainyalley.architecture.common.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.util.MissingResourceException;

public class EhCacheProvider implements CacheProvider {

    private final Ehcache ehcache;

    public EhCacheProvider(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    @Override
    public boolean put(String key, Object value) {
        this.ehcache.put(new Element(key, value));
        return true;
    }

    @Override
    public <V> V get(String key, Class<V> type) throws MissingResourceException {
        Element element = null;
        try {
            element = this.ehcache.get(key);
        } catch (Exception e) {
            throw new MissingResourceException("Cache missing", type.getName(), key);
        }

        if (element == null) {
            throw new MissingResourceException("Cache missing", type.getName(), key);
        }

        Object value = element.getObjectValue();
        return type.cast(value);
    }

}
