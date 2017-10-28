package com.rainyalley.architecture.core.notification;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class HashMapBroadCast implements BroadCast {

    private Map<Object,List<Subscriber>> map = new HashMap<Object, List<Subscriber>>();

    private Lock lock = new ReentrantLock();

    @Override
    public void subscribe(Object topic, Subscriber subscriber) {
        List<Subscriber> subscriberList = map.get(topic);
        if(subscriberList != null){
            subscriberList.add(subscriber);
            return;
        }

        lock.lock();
        try {
            subscriberList = map.get(topic);
            if(subscriberList != null){
                subscriberList.add(subscriber);
                return;
            }
            subscriberList = new ArrayList<Subscriber>();
            map.put(topic, subscriberList);
            subscriberList.add(subscriber);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void publish(Object topic, Object message) {
        List<Subscriber> subscriberList = map.get(topic);
        if(CollectionUtils.isEmpty(subscriberList)){
            return;
        }

        for (Subscriber subscriber : subscriberList) {
            subscriber.receive(topic, message);
        }
    }
}
