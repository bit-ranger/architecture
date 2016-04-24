package com.rainyalley.architecture.core.notification;

public interface Observer<T> {


    void focus(Event event, T subject);

}
