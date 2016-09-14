package com.rainyalley.architecture.core.notification;

public interface NotificationManager {


    /**
     * 关注一个事件类型
     *
     * @param eventType 事件的类型
     * @param observer  关注者
     * @param <E>       时间的类型
     */
    <E extends Event<?>> void subscribe(Class<E> eventType, Observer<E> observer);

    /**
     * 事件通知
     *
     * @param event
     * @param <E>
     */
    <E extends Event<?>> void notify(E event);
}
