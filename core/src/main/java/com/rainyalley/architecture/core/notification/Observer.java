package com.rainyalley.architecture.core.notification;

public interface Observer<T extends Event<?>> {


    void focus(T event);

}
