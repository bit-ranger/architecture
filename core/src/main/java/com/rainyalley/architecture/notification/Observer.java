package com.rainyalley.architecture.notification;

public interface Observer<T extends Event<?>> {


    void focus(T event);

}
