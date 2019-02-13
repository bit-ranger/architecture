package com.rainyalley.architecture.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 总线简单实现
 * @author bin.zhang
 */
public class SimpleBus implements Bus {

    private Map<Topic,List<Subscriber>> map = new HashMap<>();

    @Override
    public void subscribe(Topic topic, Subscriber subscriber) {
        List<Subscriber> subscriberList = map.computeIfAbsent(topic, k -> new ArrayList<>());
        subscriberList.add(subscriber);
    }

    @Override
    public void publish(Topic topic, Event event, Publisher publisher) {
        List<Subscriber> subscriberList = map.get(topic);
        if(subscriberList == null || subscriberList.isEmpty()){
            return;
        }

        for (Subscriber subscriber : subscriberList) {
            subscriber.onEvent(topic, event, publisher);
        }
    }
}
