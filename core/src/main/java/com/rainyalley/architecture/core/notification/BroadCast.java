package com.rainyalley.architecture.core.notification;

public interface BroadCast {

    /**
     * 订阅
     * @param topic 话题
     * @param subscriber 订户
     */
    void subscribe(Object topic, Subscriber subscriber);

    /**
     * 发布消息
     * @param topic 话题
     * @param message 消息
     */
    void publish(Object topic, Object message);
}
