package com.rainyalley.architecture.event;

/**
 * 总线
 * @author bin.zhang
 */
public interface Bus {

    /**
     * 订阅
     * @param topic 话题
     * @param subscriber 订户
     */
    void subscribe(Topic topic, Subscriber subscriber);

    /**
     * 发布事件
     * @param topic 话题
     * @param event 事件
     * @param publisher 发布者
     */
    void publish(Topic topic, Event event, Publisher publisher);
}
