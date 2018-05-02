package com.rainyalley.architecture.filter.limit;

import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 从Map中获取指定的初始值
 * @author bin.zhang
 */
@ThreadSafe
public class MapValueInitial<K,V> {

    private Map<K,V> map;

    private ReentrantLock lock = new ReentrantLock();

    private Callable<V> valueInitial;

    public MapValueInitial(Map<K, V> map, Callable<V> valueInitial) {
        this.map = map;
        this.valueInitial = valueInitial;
    }

    public V get(K key){
        V val = map.get(key);
        if(val != null){
            return val;
        }

        lock.lock();
        try{
            val = map.get(key);
            if(val != null){
                return val;
            } else {
                try {
                    val = valueInitial.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(key, val);
            return val;
        } finally {
            lock.unlock();
        }
    }
}
