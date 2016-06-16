package com.rainyalley.architecture.core.notification;

import java.util.ArrayList;
import java.util.List;

public class DefaultNotificationManager implements NotificationManager {


    private List<Advisor> advisors = new ArrayList<Advisor>();

    private <E extends Event<?>> Advisor<E> get(Class<E> eventType){
        for (Advisor advisor : advisors) {
            if(advisor.eventType.equals(eventType)){
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
        Advisor<E> advisor = get(eventType);
        if(advisor == null){
            advisor = new Advisor<E>(eventType);
            advisors.add(advisor);
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
        Advisor<E> advisor = get(eventType);
        advisor.notify(event);
    }


    private static class Advisor<T extends Event<?>>{

        private Class<T> eventType;

        private List<Observer<T>> observerList = new ArrayList<Observer<T>>(4);

        public Advisor(Class<T> eventType) {
            this.eventType = eventType;
        }

        private void push(Observer<T> observer){
            this.observerList.add(observer);
        }

        private void notify(T event){
            for (Observer<T> observer : observerList) {
                observer.focus(event);
            }
        }

    }

}
