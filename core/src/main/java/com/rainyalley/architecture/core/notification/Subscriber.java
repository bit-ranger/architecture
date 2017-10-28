package com.rainyalley.architecture.core.notification;

public interface Subscriber {
    void receive(Object topic, Object message);
}
