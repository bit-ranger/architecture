package com.rainyalley.architecture.core.notification;

public interface NotificationManager {


    <T> void subscribe(Class<T> subjectType, Observer<T> observer);


    <T> void notify(Event event, T subject);
}
