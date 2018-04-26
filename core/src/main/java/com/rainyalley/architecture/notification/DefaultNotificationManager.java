package com.rainyalley.architecture.notification;

import java.util.ArrayList;
import java.util.List;

public class DefaultNotificationManager implements NotificationManager {


    private final List<DefaultNotificationManager.Advisor> advisors = new ArrayList<DefaultNotificationManager.Advisor>();

    private <E extends Event<?>> DefaultNotificationManager.Advisor<E> get(Class<E> eventType) {
        for (DefaultNotificationManager.Advisor advisor : this.advisors) {
            if (advisor.eventType.equals(eventType)) {
                return advisor;
            }
        }
        return null;
    }


    /**
     * 关注一个事件类型
     *
     * @param eventType 事件的类型
     * @param observer  关注者
     */
    @Override
    public <E extends Event<?>> void subscribe(Class<E> eventType, Observer<E> observer) {
        DefaultNotificationManager.Advisor<E> advisor = this.get(eventType);
        if (advisor == null) {
            advisor = new DefaultNotificationManager.Advisor<E>(eventType);
            this.advisors.add(advisor);
        }

        advisor.push(observer);
    }

    /**
     * 事件通知
     *
     * @param event
     */
    @Override
    public <E extends Event<?>> void notify(E event) {
        Class<E> eventType = (Class<E>) event.getClass();
        DefaultNotificationManager.Advisor<E> advisor = this.get(eventType);
        advisor.notify(event);
    }


    private static class Advisor<T extends Event<?>> {

        private final Class<T> eventType;

        private final List<Observer<T>> observerList = new ArrayList<Observer<T>>(4);

        public Advisor(Class<T> eventType) {
            this.eventType = eventType;
        }

        private void push(Observer<T> observer) {
            observerList.add(observer);
        }

        private void notify(T event) {
            for (Observer<T> observer : this.observerList) {
                observer.focus(event);
            }
        }

    }

}
