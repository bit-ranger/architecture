package com.rainyalley.architecture.core.notification;

import java.util.ArrayList;
import java.util.List;

public class DefaultNotificationManager implements NotificationManager {


    private List<Advisor> advisors = new ArrayList<Advisor>();

    @Override
    public <T> void subscribe(Class<T> subjectType, Observer<T> observer) {
        Advisor<T> advisor = get(subjectType);
        if(advisor == null){
            advisor = new Advisor<T>(subjectType);
            advisors.add(advisor);
        }

        advisor.push(observer);
    }

    @Override
    public void notify(Event event, Object subject) {
        Class<?> subjectType = subject.getClass();
        for (Advisor advisor : getSupers(subjectType)) {
            advisor.notify(event, subject);
        }
    }

    private <T> Advisor<T> get(Class<T> subjectType){
        for (Advisor advisor : advisors) {
            if(advisor.subjectType.equals(subjectType)){
                return advisor;
            }
        }
        return null;
    }

    private List<Advisor> getSupers(Class<?> subjectType){
        List<Advisor> result = new ArrayList<Advisor>(2);
        for (Advisor advisor : advisors) {
            if(advisor.subjectType.isAssignableFrom(subjectType)){
                result.add(advisor);
            }
        }
        return result;
    }

    private static class Advisor<T>{

        private Class<T> subjectType;

        private List<Observer<T>> observerList = new ArrayList<Observer<T>>(4);

        public Advisor(Class<T> subjectType) {
            this.subjectType = subjectType;
        }

        private void push(Observer<T> observer){
            this.observerList.add(observer);
        }

        private void notify(Event event, T subject){
            for (Observer<T> observer : observerList) {
                observer.focus(event, subject);
            }
        }

    }

}
