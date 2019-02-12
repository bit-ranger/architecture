package com.rainyalley.architecture.event;

/**
 * 事件订阅者
 * @author bin.zhang
 */
public interface Subscriber {

    /**
     * 监听
     * @param topic
     * @param event
     * @param publisher
     */
    void onEvent(Topic topic, Event event, Publisher publisher);
}
